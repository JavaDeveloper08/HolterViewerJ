package mvc;

import java.time.LocalDateTime;

import data.*;
import adapters.*;
import parsers.*;
import jssc.SerialPortException;

public class AppModel {
	private AppController controller;
	
	private Patient patient;
	private Time exam_time;
	private Sample single_sample;
	
	private COMPortAdapter PortCOM;
	static private int portComBaudrate = 115200;
	private byte[] comPortCommandFrame = new byte[4];
	private byte[] comPortTimeFrame = new byte[9];
	
	private FileAdapter resultFile;
	private String csvCellSeparator = ",";
	private String csvLineSeparator = System.lineSeparator();
	
	private AppDataParser appParser;
	private int dataReadyFlag;
	private boolean downloadDataFlag;
	
	/** default constructors */
	public AppModel (){
		controller = null;
		patient = new Patient();
		exam_time = new Time();
		single_sample = new Sample();
		PortCOM =  new COMPortAdapter();
		resultFile = new FileAdapter();
		appParser = new AppDataParser();
		dataReadyFlag = 0;
		downloadDataFlag = false;
		comPortCommandFrame[0] = (byte)(0xEF);
		comPortCommandFrame[1] = (byte)(0xFE);
	}
	
	/** setters and getters */
	public void setController(AppController c){
		this.controller = c;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getDataReadyFlag() {
		return dataReadyFlag;
	}
	
	public Time getExam_time() {
		return exam_time;
	}

	public Sample getSingle_sample() {
		return single_sample;
	}
	
	public boolean getDownloadDataFlag() {
		return downloadDataFlag;
	}

	public void setDownloadDataFlag(boolean downloadDataFlag) {
		this.downloadDataFlag = downloadDataFlag;
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
	
	public void readBytes () {
		byte[] comPortFrame;
		comPortFrame = PortCOM.readBytesFromPort();
		appParser.parse(comPortFrame);
		if(appParser.getHeader_recevied() == true){
			appParser.setHeader_recevied(false);
			exam_time = appParser.getTime_data();
			dataReadyFlag = 1;
		}
		else if (appParser.getSample_recevied() == true){
			appParser.setSample_recevied(false);
			single_sample = appParser.getSample_data();
			dataReadyFlag = 2;
		}
		else
			dataReadyFlag = 0;
	}
	
	public void sendCommands (int code, int value) {	
		comPortCommandFrame[2] = (byte)(code);
		comPortCommandFrame[3] = (byte)(value);
	
		try{
			PortCOM.writeBytesToPort(comPortCommandFrame);
		}catch (SerialPortException e) {
			e.printStackTrace();
		}
			
	}
	
	public void sendTime (){
		LocalDateTime localTime = LocalDateTime.now();
		comPortTimeFrame[2] = (byte)(5);
		comPortTimeFrame[3] = (byte)(localTime.getSecond());
		comPortTimeFrame[4] = (byte)(localTime.getMinute());
		comPortTimeFrame[5] = (byte)(localTime.getHour());
		comPortTimeFrame[6] = (byte)(localTime.getDayOfMonth());
		comPortTimeFrame[7] = (byte)(localTime.getMonthValue());
		comPortTimeFrame[8] = (byte)(localTime.getYear());
		
		try{
			PortCOM.writeBytesToPort(comPortTimeFrame);
		}catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	
	public void createResultFile (String file_name){
		String dataDirPath = System.getProperty("user.dir") + "/" + "data" + "/";
		FileAdapter.createDirectory(dataDirPath);
		resultFile = new FileAdapter(dataDirPath + file_name + ".csv", csvCellSeparator, csvLineSeparator);
		resultFile.openOrCreateToWrite();
	}
	
	public void writePatientdDataToFile () {
		String[] p = {patient.getName_(),patient.getLast_name_(),patient.getID_num_()};
		resultFile.writeCSVLine(p);
	}
	
	public void writeDataToFile() {
		if(dataReadyFlag == 1){
			resultFile.writeLine(exam_time.toString());
		}
		else if(dataReadyFlag == 2) {
			resultFile.writeLine(single_sample.toString());
		}
	}
	
}
