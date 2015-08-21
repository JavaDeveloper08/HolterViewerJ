package parsers;

import data.*;

/**
 * @class FileDataParser
 * @brief parser to data from file
 */
public class FileDataParser {
	private Patient patient;
	private Time startExamTime;
	private Time stopExamTime;
	
	public FileDataParser(){
		patient = new Patient();
		startExamTime = new Time();
		stopExamTime = new Time();
	}
	
	/**
	 * Getters to class fields
	 */
	public Patient getPatient() {
		return patient;
	}

	public Time getStartExamTime() {
		return startExamTime;
	}

	public Time getStopExamTime() {
		return stopExamTime;
	}

	/**
	 * @methods parseHeader()
	 * @brief parse String to patient object
	 * @param line - String data
	 */
	public void parseHeader(String header) {
		String tmp;
		int head_index = 0;
		int tail_index = 0;
		
		tail_index = header.indexOf(',', head_index);
		tmp = header.substring(head_index,tail_index);
		patient.setName_(tmp);
		
		head_index = tail_index + 1;
		tail_index = header.indexOf(',', head_index);
		tmp = header.substring(head_index,tail_index);
		patient.setLast_name_(tmp);
		
		head_index = tail_index + 1;
		tmp = header.substring(head_index,header.length());
		patient.setID_num_(tmp);
	}
	
	/**
	 * @methods parseTime()
	 * @brief parse String to Time object
	 * @param line - String data
	 * @param source - start or stop time
	 */
	public void parseTime(String line, int source) {
		String tmp;
		int head_index = 0;
		int tail_index = 0;
		
		tail_index = line.indexOf(',', head_index);
		tmp = line.substring(head_index,tail_index);
		if(source == 1)
			startExamTime.setYear_(Integer.valueOf(tmp));
		else if(source == 2)
			stopExamTime.setYear_(Integer.valueOf(tmp));
		
		head_index = tail_index + 1;
		tail_index = line.indexOf(',', head_index);
		tmp = line.substring(head_index,tail_index);
		if(source == 1)
			startExamTime.setMonth_(Integer.valueOf(tmp));
		else if(source == 2)
			stopExamTime.setMonth_(Integer.valueOf(tmp));
		
		head_index = tail_index + 1;
		tail_index = line.indexOf(',', head_index);
		tmp = line.substring(head_index,tail_index);
		if(source == 1)
			startExamTime.setDay_(Integer.valueOf(tmp));
		else if(source == 2)
			stopExamTime.setDay_(Integer.valueOf(tmp));
		
		head_index = tail_index + 1;
		tail_index = line.indexOf(',', head_index);
		tmp = line.substring(head_index,tail_index);
		if(source == 1)
			startExamTime.setHour_(Integer.valueOf(tmp));
		else if(source == 2)
			stopExamTime.setHour_(Integer.valueOf(tmp));
		
		head_index = tail_index + 1;
		tmp = line.substring(head_index,line.length());
		if(source == 1)
			startExamTime.setMinute_(Integer.valueOf(tmp));
		else if(source == 2)
			stopExamTime.setMinute_(Integer.valueOf(tmp));
	}
	
	/**
	 * @methods parseSample()
	 * @brief parse String to sample object
	 * @param String data
	 */
	public Sample parseSample (String line){
		Sample tmp = new Sample();
		String s_tmp;
		int head_index = 0;
		int tail_index = 0;
		
		tail_index = line.indexOf(',', head_index);
		s_tmp = line.substring(head_index,tail_index);
		
		tmp.setTimestamp_(Integer.valueOf(s_tmp));
		
		head_index = tail_index + 1;
		s_tmp = line.substring(head_index,line.length());
		
		tmp.setSignal_sample_(Double.valueOf(s_tmp));
		
		return tmp;
	}
}
