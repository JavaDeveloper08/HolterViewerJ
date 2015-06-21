package mvc;

import java.awt.event.*;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public class AppController implements ActionListener, SerialPortEventListener {
	private AppModel cModel = null;
	private AppView cView = null;
	private static final int STREAM_DATA_CMD = 1;
	private static final int SAVE_DATA_CMD = 2;
	private static final int DOWNLOAD_DATA_CMD = 3;
	private static final int ERASE_DATA_CMD = 4;
	
	/** parameterized constructors */
	public AppController (AppModel model, AppView view){
		this.cModel = model;
		this.cView = view;
		this.cModel.setController(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Object actionSource = e.getSource();
		
		switch(actionCommand){
			case "Close":
				System.exit(0);
				break;
			
			case "About":
				cView.setAboutFrame();
				break;
			case "Viewer":
				//cView.setViewerFrame();
				break;
			case "Clear":
				cView.cleanPatientView();
				break;
			
			case "Scan":
				cView.setPortNames(cModel.scan());
				break;
			case "Open Port":
				cModel.open(cView.getUserPort());
				cView.openPortAction();
				break;
			case "Close Port":
				cModel.close();
				cView.closePortAction();
				break;
			
			case "Stream data":
				cModel.sendCommands(STREAM_DATA_CMD, 1);
				break;
			case "Save data":
				cModel.sendCommands(SAVE_DATA_CMD, 1);
				break;
			case "Download":
				try{
				cModel.createResultFile(cView.getFileName());
				cModel.setPatient(cView.readPatientView());
				cModel.writePatientdDataToFile();
				} catch (AppException exception){
					exception.show_exception(); 
				}
				cModel.setDownloadDataFlag(true);
				cModel.sendCommands(DOWNLOAD_DATA_CMD, 1);
				
				break;
			case "Erase data":
				cModel.sendCommands(ERASE_DATA_CMD, 1);
				break;
			case "Time send":
				break;
		}
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.isRXCHAR()) {
			cModel.readBytes();
			if(cModel.getDataReadyFlag() == 1){
				cView.setDateView(cModel.getExam_time());
				cView.setTimeView(cModel.getExam_time());
			}
			else if(cModel.getDataReadyFlag() == 2){
				cView.addSampleToChart(cModel.getSingle_sample());
			}
			
			if(cModel.getDownloadDataFlag() == true)
				cModel.writeDataToFile();
		}
		
	}
}
