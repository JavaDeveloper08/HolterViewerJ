package mvc;

import java.awt.event.*;

import mvc.models.AppMainModel;
import mvc.models.AppViewerModel;
import mvc.views.AppMainView;
import mvc.views.AppViewerView;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

/**
 * @class AppController
 * @brief class representing application control. Implements user interaction, model and views update.
 * @implements ActionListener, SerialPortEventListener
 */

public class AppController implements ActionListener, SerialPortEventListener {
	private AppMainModel cMainModel = null;
	private AppViewerModel cViewerModel = null;
	private AppMainView cMainView = null;
	private AppViewerView cViewerView = null;
	
	/** parameterized constructors */
	public AppController (AppMainModel mmodel, AppMainView mview, AppViewerModel vmodel, AppViewerView vview){
		this.cMainModel = mmodel;
		this.cMainView = mview;
		this.cMainModel.setController(this);
		this.cViewerModel = vmodel;
		this.cViewerView = vview;
		this.cViewerModel.setController(this);
	}

	/**
	 * @fn actionPerformed()
	 * @brief action event handler
	 * @param action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Object actionSource = e.getSource();
		
		switch(actionCommand){
			case "Close":
				if(actionSource == cViewerView.getAppViewerButtonClose()){
					cViewerView.clearChart();
					cViewerView.clearViewer();
					cViewerModel.dataClear();
					cViewerView.dispose();
				}
				else
					System.exit(0);
				break;
			
			case "About":
				cMainView.setAboutFrame();
				break;
			
			case "Viewer":
				this.cViewerView.setViewerFrameVisible();
				break;
			
			case "Clear":
				cMainView.cleanPatientView();
				break;
			
			case "Scan":
				cMainView.setPortNames(cMainModel.scan());
				break;
			case "Open Port":
				cMainModel.open(cMainView.getUserPort());
				cMainView.openPortAction();
				cMainModel.sendCommands(cMainModel.getGetStateCmd(), 1);
				break;
			case "Close Port":
				cMainModel.close();
				cMainView.closePortAction();
				break;
			
			case "Stream data":
				if(cMainView.getAppButtonDataStream().isSelected() == true)
					cMainModel.sendCommands(cMainModel.getStreamDataCmd(), 1);	
				else
					cMainModel.sendCommands(cMainModel.getStreamDataCmd(), 0);
				break;
				
			case "Save data":
				if(cMainView.getAppButtonDataSave().isSelected() == true)
					cMainModel.sendCommands(cMainModel.getSaveDataCmd(), 1);
				else
					cMainModel.sendCommands(cMainModel.getSaveDataCmd(), 0);
				break;
				
			case "Download":
				if(cMainView.getAppButtonDataLoad().isSelected() == true){
					try{
					cMainModel.createResultFile(cMainView.getFileName());
					cMainModel.setPatient(cMainView.readPatientView());
					cMainModel.writePatientdDataToFile();
					cMainModel.setDownloadDataFlag(1);
					cMainView.setProgressBarInc(0);
					cMainModel.sendCommands(cMainModel.getDownloadDataCmd(), 1);
					} catch (AppException exception){
						cMainView.getAppButtonDataLoad().setSelected(false);
						exception.show_exception(); 
					}
				}
				else {
					cMainModel.setDownloadDataFlag(0);
					cMainModel.closeFile();
				}
				
				break;
			
			case "Erase data":
				cMainModel.sendCommands(cMainModel.getEraseDataCmd(), 1);
				break;
			
			case "Time send":
				cMainModel.sendTime();
				break;
			
			case "Get state":
				cMainModel.sendCommands(cMainModel.getGetStateCmd(), 1);
				break;
			
			case "Open file":
				try {
					cViewerModel.loadData(cViewerView.getSelectedPath());
				}catch (AppException exception){
					exception.show_exception(); 
				}
				cViewerView.setInfo(cViewerModel.getPatient(), cViewerModel.getStartExamTime(), cViewerModel.getStopExamTime());
				cViewerView.upgradeChart(cViewerModel.getData_view_sample());
				break;
				
			case "Show":
				try {
					cViewerModel.findSignalSection(cViewerView.getTimeSpinnerFrom(), cViewerView.getTimeSpinnerTo());
				}catch (AppException exception){
					exception.show_exception(); 
				}
				cViewerView.upgradeChart(cViewerModel.getData_view_sample());
				break;
				
			case "DC":
				cMainModel.getAppParser().setFilterDC_enable(cMainView.getAppCheckBoxFilterDC().isSelected());
				break;
			case "50Hz":
				cMainModel.getAppParser().setFilter50Hz_enable(cMainView.getAppCheckBoxFilter50Hz().isSelected());
				break;
		}
	}

	/**
	 * @fn serialEvent()
	 * @brief serial port event handler
	 * @param serial port event
	 */
	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.isRXCHAR()) {
			cMainModel.readBytes();
			if(cMainModel.getDataReadyFlag() == 1){
				cMainView.setTimeView(cMainModel.calculate_current_time());
				if(cMainModel.getDownloadDataFlag() == 0){
					cMainView.addSampleToChart(cMainModel.getPacket_sample());
				}
				else if((cMainModel.getDownloadDataFlag() == 1) && (cMainModel.getSamples_counter() > cMainModel.getPrecentOfData())) {
					cMainModel.setSamples_counter(0);
					cMainView.setProgressBarInc(1);
				}
			}
			
			if(cMainModel.getDownloadDataFlag() == 2){
				cMainModel.setDownloadDataFlag(0);
				cMainView.setProgressBarInc(2);
				cMainView.set_download_state(false);
			}
			
			if(cMainModel.isGetStateFlag() == true){
				cMainView.setDateView(cMainModel.getExam_time());
				cMainView.setTimeView(cMainModel.getExam_time());
				
				cMainView.set_stream_state(cMainModel.getDevice_state()[0]);
				cMainView.set_run_state(cMainModel.getDevice_state()[1]);
				cMainView.set_save_state(cMainModel.getDevice_state()[2]);
				cMainView.set_error_state(cMainModel.getDevice_state()[3]);
			}
		}
	}
}
