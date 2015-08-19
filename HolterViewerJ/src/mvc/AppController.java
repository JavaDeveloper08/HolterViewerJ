package mvc;

import java.awt.event.*;

import mvc.models.AppMainModel;
import mvc.models.AppViewerModel;
import mvc.views.AppMainView;
import mvc.views.AppViewerView;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public class AppController implements ActionListener, SerialPortEventListener {
	private AppMainModel cMainModel = null;
	private AppViewerModel cViewerModel = null;
	private static AppMainView cMainView = null;
	private AppViewerView cViewerView = null;
	
	private static final int STREAM_DATA_CMD = 1;
	private static final int SAVE_DATA_CMD = 2;
	private static final int DOWNLOAD_DATA_CMD = 3;
	private static final int ERASE_DATA_CMD = 4;
	private static final int GET_STATE_CMD = 6;
	
	private int progressBarValue;
	
	public AppController (AppMainModel model, AppMainView view){
		this.cMainModel = model;
		this.cMainView = view;
		this.cViewerModel = new AppViewerModel();
		this.cViewerView = new AppViewerView();
		this.cMainModel.setController(this);
		this.cViewerModel.setController(this);
		progressBarValue = 0;
	}
	
	public void setControllerToViewer() {
		this.cViewerModel.setController(this);
		this.cViewerView.setController(this);
		this.cViewerView.setViewerFrameVisible();
	}
	
	public static AppMainView getMainView() {
		return cMainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Object actionSource = e.getSource();
		
		switch(actionCommand){
			case "Close":
				if(actionSource == cViewerView.getAppViewerButtonClose()){
					cViewerView.clearChart();
					cViewerView.dispose();
				}
				else
					System.exit(0);
				break;
			
			case "About":
				cMainView.setAboutFrame();
				break;
			
			case "Viewer":
				this.setControllerToViewer();
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
				cMainModel.sendCommands(GET_STATE_CMD, 1);
				break;
			case "Close Port":
				cMainModel.close();
				cMainView.closePortAction();
				break;
			
			case "Stream data":
				if(cMainView.getAppButtonDataStream().isSelected() == true)
					cMainModel.sendCommands(STREAM_DATA_CMD, 1);	
				else
					cMainModel.sendCommands(STREAM_DATA_CMD, 0);
				break;
				
			case "Save data":
				if(cMainView.getAppButtonDataSave().isSelected() == true)
					cMainModel.sendCommands(SAVE_DATA_CMD, 1);
				else
					cMainModel.sendCommands(SAVE_DATA_CMD, 0);
				break;
				
			case "Download":
				if(cMainView.getAppButtonDataLoad().isSelected() == true){
					try{
					cMainModel.createResultFile(cMainView.getFileName());
					cMainModel.setPatient(cMainView.readPatientView());
					cMainModel.writePatientdDataToFile();
					cMainModel.setDownloadDataFlag(1);
					cMainView.setProgressBarValue(0);
					progressBarValue = 0;
					cMainModel.sendCommands(DOWNLOAD_DATA_CMD, 1);
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
				cMainModel.sendCommands(ERASE_DATA_CMD, 1);
				break;
			
			case "Time send":
				cMainModel.sendTime();
				break;
			
			case "Get state":
				cMainModel.sendCommands(GET_STATE_CMD, 1);
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
					cViewerModel.findSignalPeriod(cViewerView.getTimeSpinnerFrom(), cViewerView.getTimeSpinnerTo());
				}catch (AppException exception){
					exception.show_exception(); 
				}
				cViewerView.upgradeChart(cViewerModel.getData_view_sample());
				break;
		}
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.isRXCHAR()) {
			cMainModel.readBytes();
			if(cMainModel.getDataReadyFlag() == 1){
				cMainView.setTimeView(cMainModel.calculate_current_time());
				if((cMainModel.getDownloadDataFlag() == 1) && (cMainModel.getSamples_counter() > cMainModel.getPrecentOfData())) {
					cMainModel.setSamples_counter(0);
					cMainView.setProgressBarValue(++progressBarValue);
				}
			}
			
			if(cMainModel.getDownloadDataFlag() == 2){
				cMainModel.setDownloadDataFlag(0);
				cMainView.setProgressBarValue(100);
				cMainView.set_download_state(false);
			}
			
			if(cMainModel.isGetStateFlag() == true){
				cMainView.setDateView(cMainModel.getExam_time());
				cMainView.setTimeView(cMainModel.getExam_time());
				
				cMainView.set_stream_state(cMainModel.getDevice_state()[0]);
				cMainView.set_run_state(cMainModel.getDevice_state()[1]);
				cMainView.set_save_state(cMainModel.getDevice_state()[2]);
			}
		}
		
	}
}
