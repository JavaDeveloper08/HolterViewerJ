package mvc;

import data.*;

import java.awt.event.*;

import javax.swing.*;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

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
			case "Clear":
				cView.cleanPatientView();
				break;
			case "Scan":
				cView.setPortNames(cModel.scan());
				break;
			case "Open Port":
				cModel.open(cView.getUserPort());
				break;
			case "Close Port":
				cModel.close();
				break;
			case "Download":
				cModel.createResultFile(cView.getFileName());
				break;
				
		}
	}

	@Override
	public void serialEvent(SerialPortEvent e) {
		if (e.isRXCHAR()) {
			
		}
		
	}
}
