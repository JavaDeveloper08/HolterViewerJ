package mvc.models;

import java.time.LocalDateTime;
import java.util.Vector;

import mvc.*;
import data.*;
import adapters.*;
import parsers.*;
import jssc.SerialPortException;

/**
 * @class AppMainModel
 * @brief class representing all main application date, application state and allows access to them
 */

public class AppMainModel {
	/** Controller*/
	private AppController controller;
	
	/** Data */
	private Patient patient;
	private Time exam_time;
	private Time start_exam_time;
	private Time stop_exam_time;
	private Vector<Sample> packet_sample;
	private long samples_counter;
	private int precentOfData;
	private Boolean[] device_state = new Boolean[4];
	
	/** COMMUNICATION */
	/* Serial Port*/
	private SerialPortAdapter PortCOM;
	private int portComBaudrate = 9600;
	private byte[] comPortCommandFrame = new byte[4];
	private byte[] comPortTimeFrame = new byte[10];
	
	/* Parser */
	private SerialPortDataParser appParser;
	
	/* Commands */
	private final int STREAM_DATA_CMD = 1;
	private final int SAVE_DATA_CMD = 2;
	private final int DOWNLOAD_DATA_CMD = 3;
	private final int ERASE_DATA_CMD = 4;
	private final int TIME_DATA_CMD = 5;
	private final int GET_STATE_CMD = 6;
	private final int SEND_NEXT_CMD = 7;
	
	/* General flag */
	private int dataReadyFlag;
	private int downloadDataFlag;
	private boolean getStateFlag;
	
	/* File */
	private FileAdapter resultFile;
	private String csvCellSeparator = ",";
	private String csvLineSeparator = System.lineSeparator();
	
	/** default constructors */
	public AppMainModel (){
		controller = null;
		
		patient = new Patient();
		exam_time = new Time();
		start_exam_time = new Time();
		stop_exam_time = new Time();
		packet_sample = new Vector<Sample>(SerialPortDataParser.getDataSamplesNumber());
		samples_counter = 0;
		precentOfData = 0;
		
		PortCOM =  new SerialPortAdapter();
		appParser = new SerialPortDataParser();
		resultFile = new FileAdapter();
		
		dataReadyFlag = 0;
		downloadDataFlag = 0;
		getStateFlag = false;
		
		comPortCommandFrame[0] = (byte)(0xEF);
		comPortCommandFrame[1] = (byte)(0xFE);
		comPortTimeFrame[0] = (byte)(0xEF);
		comPortTimeFrame[1] = (byte)(0xFE);
	}
	
	/**
	 * Getters and setters to class fields
	 */
	public void setController(AppController c){
		this.controller = c;
	}

	public SerialPortDataParser getAppParser() {
		return appParser;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Time getExam_time() {
		return exam_time;
	}
	
	public void setStart_exam_time(Time start_exam_time) {
		this.start_exam_time = start_exam_time;
	}

	public void setStop_exam_time(Time stop_exam_time) {
		this.stop_exam_time = stop_exam_time;
	}
	
	public Vector<Sample> getPacket_sample() {
		return packet_sample;
	}
	
	public long getSamples_counter() {
		return samples_counter;
	}

	public void setSamples_counter(long samples_counter) {
		this.samples_counter = samples_counter;
	}
	
	public long getPrecentOfData() {
		return precentOfData;
	}
	
	public int getDataReadyFlag() {
		return dataReadyFlag;
	}
	
	public void setDataReadyFlag(int dataReadyFlag) {
		this.dataReadyFlag = dataReadyFlag;
	}

	public int getDownloadDataFlag() {
		return downloadDataFlag;
	}

	public void setDownloadDataFlag(int downloadDataFlag) {
		this.downloadDataFlag = downloadDataFlag;
	}
	
	public boolean isGetStateFlag() {
		return getStateFlag;
	}

	public void setGetStateFlag(boolean getStateFlag) {
		this.getStateFlag = getStateFlag;
	}
	
	public Boolean[] getDevice_state() {
		return device_state;
	}

	public int getStreamDataCmd() {
		return STREAM_DATA_CMD;
	}

	public int getSaveDataCmd() {
		return SAVE_DATA_CMD;
	}

	public int getDownloadDataCmd() {
		return DOWNLOAD_DATA_CMD;
	}

	public int getEraseDataCmd() {
		return ERASE_DATA_CMD;
	}

	public int getGetStateCmd() {
		return GET_STATE_CMD;
	}
	
	/** COMMUNICATION - SERIAL PORT */
	
	/**
	 * @methods open()
	 * @brief serial port open
	 * @param port name
	 */
	public void open(String portName){
		try {
			PortCOM.connect(portName, portComBaudrate, 8, 1, 0);
			PortCOM.startPortListening(this.controller);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @methods close()
	 * @brief serial port close
	 */
	public void close(){
		try {
			PortCOM.stopPortListening();
			PortCOM.disconnect();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * @methods scan()
	 * @brief scan serial port and find port names
	 * @return String array with port names
	 */
	public String[] scan(){
		return PortCOM.getPortsNames();
	}
	
	/** COMMUNICATION - COMMANDS */
	
	/**
	 * @methods sendCommands()
	 * @brief send commands to device
	 * @param commands code
	 * @param value to send
	 */
	public void sendCommands (int code, int value) {	
		comPortCommandFrame[2] = (byte)(code);
		comPortCommandFrame[3] = (byte)(value);
	
		try{
			PortCOM.writeBytesToPort(comPortCommandFrame);
		}catch (SerialPortException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * @methods sendTime()
	 * @brief send current system time to device
	 */
	public void sendTime() {
		LocalDateTime localTime = LocalDateTime.now();
		comPortTimeFrame[2] = (byte)(TIME_DATA_CMD);
		comPortTimeFrame[3] = (byte)(localTime.getSecond());
		comPortTimeFrame[4] = (byte)(localTime.getMinute());
		comPortTimeFrame[5] = (byte)(localTime.getHour());
		comPortTimeFrame[6] = (byte)(localTime.getDayOfMonth());
		comPortTimeFrame[7] = (byte)(localTime.getMonthValue());
		comPortTimeFrame[8] = (byte)(localTime.getYear() >> 8);
		comPortTimeFrame[9] = (byte)(localTime.getYear());
		
		try{
			PortCOM.writeBytesToPort(comPortTimeFrame);
		}catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
	
	/** COMMUNICATION - PARSER */
	
	/**
	 * @methods clear_flags()
	 * @brief clear parser flags
	 */
	public void clear_flags (){
		dataReadyFlag = 0;
		getStateFlag = false;
	}
	
	/**
	 * @methods readBytes()
	 * @brief read bytes form serial port, parser and set model state
	 */
	public void readBytes () {
		byte[] comPortFrame;
		comPortFrame = PortCOM.readBytesFromPort();
		appParser.parse(comPortFrame);
		this.clear_flags();
		
		if (appParser.getSample_recevied() == true){
			packet_sample = appParser.getSample_data();
			
			if(downloadDataFlag == 1){
				writeDataToFile();
				samples_counter += 30;
				sendCommands(SEND_NEXT_CMD, 0);
			}

			dataReadyFlag = 1;
		}
		else if (appParser.getState_received() == true){
			exam_time = appParser.getTime_data();
			set_state(appParser.getDevice_state());
			getStateFlag = true;
		}
		else if (appParser.getHeader_time_received() == true){
			start_exam_time = appParser.getStart_time();
			resultFile.writeLine(start_exam_time.toString());
			
			stop_exam_time = appParser.getStop_time();
			resultFile.writeLine(stop_exam_time.toString());
			
			precentOfData = calculate_progressive_bar_step(stop_exam_time, start_exam_time);
		}
		else if(appParser.getTransfer_end_received() == true){
			writeEndFileMarker();
			closeFile();
			downloadDataFlag = 2;
		}
	}

	/** COMMUNICATION - FILE */
	
	/**
	 * @methods createResultFile()
	 * @brief create result file using file name
	 * @param file name
	 */
	public void createResultFile (String file_name) {
		String dataDirPath = System.getProperty("user.dir") + "/" + "data" + "/";
		FileAdapter.createDirectory(dataDirPath);
		resultFile = new FileAdapter(dataDirPath + file_name + ".csv", csvCellSeparator, csvLineSeparator);
		resultFile.openOrCreateToWrite();
	}
	
	/**
	 * @methods writePatientdDataToFile()
	 * @brief write patient data to file (name, surname, ID number)
	 */
	public void writePatientdDataToFile() {
		String[] p = {patient.getName_(),patient.getLast_name_(),patient.getID_num_()};
		resultFile.writeCSVLine(p);
	}
	
	/**
	 * @methods writeDataToFile()
	 * @brief write samples to file
	 */
	public void writeDataToFile() {
		for(Sample i:packet_sample)
			resultFile.writeLine(i.toString());
	}
	
	/**
	 * @methods writeEndFileMarker()
	 * @brief write marker ("END") at the end of result file
	 */
	public void writeEndFileMarker() {
		resultFile.writeLine("END");
	}
	
	/**
	 * @methods closeFile()
	 * @brief close result file
	 */
	public void closeFile() {
		resultFile.close();
	}
	
	/** AUXILIARY METHODS */
	
	/**
	 * @methods calculate_current_time()
	 * @brief calculate current time from device timestamp data
	 * @return device current Time
	 */
	public Time calculate_current_time(){
		Double min = (double) (packet_sample.get(0).getTimestamp_()%60);
		Double hour = (double) (packet_sample.get(0).getTimestamp_()/60);
		exam_time.setMinute_(min.intValue());
		exam_time.setHour_(hour.intValue());
		
		return exam_time;
	}
	
	/**
	 * @methods calculate_progressive_bar_step()
	 * @brief calculate progressive bar step in number of sample using start and stop exam time
	 * @param t1 - stop exam time
	 * @param t2 - start exam time
	 * @return number of sample equals 1% of hole signal
	 */
	public int calculate_progressive_bar_step (Time t1, Time t2) {
		int step = 0;
		int seconds_number = 0;
		
		seconds_number = (t1.getDay_() - t2.getDay_())*86400 + (t1.getHour_() - t2.getHour_())*3600 + (t1.getMinute_() - t2.getMinute_())*60 + (t1.getSecond_() - t2.getSecond_());
		step = (seconds_number * 5);
		
		return step;
	}
	
	/**
	 * @methods set_state()
	 * @brief decode state code to device state array
	 * @param state code
	 */
	public void set_state(int state) {
		if((state & 0x01) != 0)
			device_state[0] = true;
		else
			device_state[0] = false;
			
		if((state & 0x02) != 0)
			device_state[1] = true;
		else
			device_state[1] = false;
		
		if((state & 0x04) != 0)
			device_state[2] = true;
		else
			device_state[2] = false;
		
		if((state & 0x08) != 0)
			device_state[3] = true;
		else
			device_state[3] = false;
	}
}
