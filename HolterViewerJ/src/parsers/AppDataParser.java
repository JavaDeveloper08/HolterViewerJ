package parsers;

import data.*;

public class AppDataParser {
	private Time time_data;
	private Sample sample_data;
	private static final byte[] header_tab = new byte[] {(byte)(0xA5), 0x5A, (byte)(0xFE)};
	private static final int data_frame_length = 11;
	private Boolean header_recevied;
	private Boolean sample_recevied;
	private Boolean start_time_received;
	private Boolean stop_time_received;
	private int device_state;
	private Boolean state_received;
	private Boolean transfer_end_received;
	
	public AppDataParser(){
		time_data = new Time();
		sample_data = new Sample();
		header_recevied = false;
		sample_recevied = false;
		start_time_received = false;
		stop_time_received = false;
		device_state = 0;
		state_received = false;
		transfer_end_received = false;
	}

	public Time getTime_data() {
		return time_data;
	}

	public void setTime_data(Time time_data) {
		this.time_data = time_data;
	}

	public Sample getSample_data() {
		return sample_data;
	}

	public void setSample_data(Sample sample_data) {
		this.sample_data = sample_data;
	}
	
	public void setHeader_recevied(Boolean header_recevied) {
		this.header_recevied = header_recevied;
	}

	public Boolean getHeader_recevied() {
		return header_recevied;
	}

	public Boolean getSample_recevied() {
		return sample_recevied;
	}

	public void setSample_recevied(Boolean sample_recevied) {
		this.sample_recevied = sample_recevied;
	}
	
	public Boolean getStart_time_received() {
		return start_time_received;
	}

	public void setStart_time_received(Boolean start_time_received) {
		this.start_time_received = start_time_received;
	}

	public Boolean getStop_time_received() {
		return stop_time_received;
	}

	public void setStop_time_received(Boolean stop_time_received) {
		this.stop_time_received = stop_time_received;
	}
	
	public Boolean getState_received() {
		return state_received;
	}

	public void setState_received(Boolean state_received) {
		this.state_received = state_received;
	}

	public int getDevice_state() {
		return device_state;
	}
	
	public Boolean getTransfer_end_received() {
		return transfer_end_received;
	}

	public void setTransfer_end_received(Boolean transfer_end_received) {
		this.transfer_end_received = transfer_end_received;
	}

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
	
	public void parse(byte [] b){
		if((b == null) || (b.length != data_frame_length)){
			header_recevied = false;
			sample_recevied = false;
			return;
		}
			
		if(is_header(b)){
			Time read_time_data  = new Time();
			int year;
			switch(b[3]){
				case 0:
					double sample = ((((int)(b[4])) & 0xFF) << 16) + ((((int)(b[5])) & 0xFF) << 8) + (((int)(b[6])) & 0xFF);
					double timestamp = ((((int)(b[7])) & 0xFF) << 24) + ((((int)(b[8])) & 0xFF) << 16) + ((((int)(b[9])) & 0xFF) << 8) + (((int)(b[10])) & 0xFF);
		
					sample_data.setSignal_sample_(sample);
					sample_data.setTimestamp_(timestamp);
					sample_recevied = true;
					break;
				
				case 1:
					read_time_data.setDay_(b[4]);
					read_time_data.setMonth_(b[5]);
					year = (((int)(b[6] & 0xFF)) << 8) | ((int)(b[7])) & 0xFF;
					read_time_data.setYear_(year);
					read_time_data.setHour_((int)(b[8] & 0xFF));
					read_time_data.setMinute_((int)(b[9] & 0xFF));
					read_time_data.setSecond_((int)(b[10] & 0xFF));
					time_data = read_time_data;
					header_recevied = true;
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
					device_state = 0;
					device_state |= b[4]; //stream
					device_state |= b[5] << 1; //run
					device_state |= b[6] << 2; //save
					device_state |= b[7] << 3; //transfer
					device_state |= b[8] << 4; //error
					state_received = true;
					break;
					
				case 5:
					transfer_end_received = true;
			}
		}
	}
}
