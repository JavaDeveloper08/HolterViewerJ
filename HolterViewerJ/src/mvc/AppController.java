package mvc;

import data.*;

import java.awt.event.*;

import javax.swing.*;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class AppController implements ActionListener, SerialPortEventListener {
	private AppModel cModel = null;
	private AppView cView = null;
	
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
			case "Download":
				try{
				cModel.createResultFile(cView.getFileName());
				cModel.setPatient(cView.readPatientView());
				cModel.writePatientdDataToFile();
				} catch (AppException exception){
					exception.show_exception(); 
				}
				cModel.setDownloadDataFlag(true);
				
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
