package mvc;

import java.util.*;

import data.*;
import adapters.*;
import parsers.*;
import jssc.SerialPortException;

public class AppModel {
	private static final Object[] String = null;

	private AppController controller;
	
	private Patient patient;
	private Time exam_time;
	private Sample single_sample;
	
	private COMPortAdapter PortCOM;
	static private int portComBaudrate = 115200;
	
	private FileAdapter resultFile;
	private String csvCellSeparator = ",";
	private String csvLineSeparator = System.lineSeparator();
	
	private AppDataParser appParser;
	
	
	/** default constructors */
	public AppModel (){
		controller = null;
		patient = new Patient();
		exam_time = new Time();
		single_sample = new Sample();
		PortCOM =  new COMPortAdapter();
		resultFile = new FileAdapter();
		appParser = new AppDataParser();
	}
	
	public void setController(AppController c){
		this.controller = c;
	}
	
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public void open(String portName){
		try {
			PortCOM.connect(portName, portComBaudrate, 8, 1, 0);
			PortCOM.startPortListening(this.controller);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			PortCOM.stopPortListening();
			PortCOM.disconnect();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
		
	public String[] scan(){
		return PortCOM.getPortsNames();
	}
	
	public boolean isConnected(){
		return PortCOM.isConnected();
	}
	
	public void createResultFile (String file_name){
		String dataDirPath = System.getProperty("user.dir") + "/" + "data" + "/";
		FileAdapter.createDirectory(dataDirPath);
		resultFile = new FileAdapter(dataDirPath + file_name + ".csv", csvCellSeparator, csvLineSeparator);
		resultFile.openOrCreateToWrite();
	}
	
	public void writePatientdData () {
		String[] p = {patient.getName_(),patient.getLast_name_(),patient.getID_num_()};
		resultFile.writeCSVLine(p);
	}
}
