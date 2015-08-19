package parsers;

import java.util.Vector;

import data.*;
import mvc.*;

/**
 * @class SerialPortDataParser
 * @brief parser to data from serial port
 */
public class SerialPortDataParser {
	private Time time_data;
	private Vector<Sample> sample_data;
	private static final byte[] header_tab = new byte[] {(byte)(0xA5), 0x5A, (byte)(0xFE)};
	private static final int data_samples_number = 30;
	private Boolean sample_recevied;
	private Boolean start_time_received;
	private Boolean stop_time_received;
	private int device_state;
	private Boolean state_received;
	private Boolean transfer_end_received;
	
	public SerialPortDataParser(){
		time_data = new Time();
		sample_data = new Vector<Sample>(data_samples_number);
		sample_recevied = false;
		start_time_received = false;
		stop_time_received = false;
		device_state = 0;
		state_received = false;
		transfer_end_received = false;
	}

	/**
	 * Getters to class fields
	 */
	public Time getTime_data() {
		return time_data;
	}

	public Vector<Sample> getSample_data() {
		return sample_data;
	}

	public Boolean getSample_recevied() {
		return sample_recevied;
	}
	
	public Boolean getStart_time_received() {
		return start_time_received;
	}

	public Boolean getStop_time_received() {
		return stop_time_received;
	}
	
	public Boolean getState_received() {
		return state_received;
	}

	public int getDevice_state() {
		return device_state;
	}
	
	public Boolean getTransfer_end_received() {
		return transfer_end_received;
	}
	
	public static int getDataSamplesNumber() {
		return data_samples_number;
	}

	/**
	 * @methods clear_all_flags()
	 * @brief clear all flags in parser
	 */
	private void clear_all_flags() {
		sample_recevied = false;
		start_time_received = false;
		stop_time_received = false;
		device_state = 0;
		state_received = false;
		transfer_end_received = false;
	}
	
	/**
	 * @methods is_header()
	 * @brief check that bytes array is header of device frame
	 * @param bytes array
	 * @return true if bytes array is a header of device frame
	 */
	private boolean is_header(byte b[]){
		if(b[0] == header_tab[0]){
			if(b[1] == header_tab[1]){
				if(b[2] == header_tab[2])
					return true;
				else
					return false;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	/**
	 * @methods parse()
	 * @brief parse bytes array to relevant data and set flags
	 * @param bytes array
	 */
	public void parse(byte [] b){
		if((b == null)){
			clear_all_flags();
			return;
		}
			
		if(is_header(b)){
			Time read_time_data  = new Time();
			int year;
			clear_all_flags();
			switch(b[3]){
				case 0:
					double timestamp = b[5]*60 + b[6];
					sample_data.clear();
					for(int i=0; i<data_samples_number; i++){
						double sample = (int)(b[i*2+7] << 8) + ((b[i*2+8]) & 0xFF);
						sample = Utils.calculateSignalData(sample);
						Sample tmp = new Sample(sample, timestamp);
						sample_data.add(tmp);
						AppController.getMainView().addSampleToChart(tmp);
					}
					sample_recevied = true;
					break;
				
				case 1:
					device_state |= b[4]; //stream
					device_state |= b[5] << 1; //run
					device_state |= b[6] << 2; //save
					device_state |= b[7] << 4; //error
					
					read_time_data.setDay_(b[8]);
					read_time_data.setMonth_(b[9]);
					year = (((int)(b[10] & 0xFF)) << 8) | ((int)(b[11])) & 0xFF;
					read_time_data.setYear_(year);
					read_time_data.setHour_((int)(b[12] & 0xFF));
					read_time_data.setMinute_((int)(b[13] & 0xFF));
					read_time_data.setSecond_((int)(b[14] & 0xFF));
					time_data = read_time_data;
					
					state_received = true;
					break;
					
				case 2:
					read_time_data.setDay_(b[4]);
					read_time_data.setMonth_(b[5]);
					year = (((int)(b[6] & 0xFF)) << 8) | ((int)(b[7])) & 0xFF;
					read_time_data.setYear_(year);
					read_time_data.setHour_((int)(b[8] & 0xFF));
					read_time_data.setMinute_((int)(b[9] & 0xFF));
					read_time_data.setSecond_((int)(b[10] & 0xFF));
					time_data = read_time_data;
					start_time_received = true;
					break;
				
				case 3:
					read_time_data.setDay_(b[4]);
					read_time_data.setMonth_(b[5]);
					year = (((int)(b[6] & 0xFF)) << 8) | ((int)(b[7])) & 0xFF;
					read_time_data.setYear_(year);
					read_time_data.setHour_((int)(b[8] & 0xFF));
					read_time_data.setMinute_((int)(b[9] & 0xFF));
					read_time_data.setSecond_((int)(b[10] & 0xFF));
					time_data = read_time_data;
					stop_time_received = true;
					break;
					
				case 4:
					transfer_end_received = true;
					break;
			}
		}
	}
}
