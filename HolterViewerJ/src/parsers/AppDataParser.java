package parsers;

import data.*;

public class AppDataParser {
	private Time time_data;
	private Sample sample_data;
	private static final byte[] header_tab = new byte[] {(byte)(0xA5), 0x5A, (byte)(0xFE)};
	private static final int data_frame_length = 11;
	private Boolean header_recevied;
	private Boolean sample_recevied;
	
	public AppDataParser(){
		time_data = new Time();
		sample_data = new Sample();
		header_recevied = false;
		sample_recevied = false;
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
		if(b.length != data_frame_length)
			return;
		if(is_header(b)){
			if(b[3] == 1){
				Time read_time_data  = new Time();
				read_time_data.setDay_(b[4]);
				read_time_data.setMonth_(b[5]);
				int year = (((int)(b[6] & 0xFF)) << 8) | (int)(b[7]) & 0xFF;
				read_time_data.setYear_(year);
				read_time_data.setHour_(b[8]);
				read_time_data.setMinute_(b[9]);
				read_time_data.setSecond_(b[10]);
				time_data = read_time_data;
				header_recevied = true;
			}
			else if(b[3] == 0){
				int sample = (((int)(b[5]) & 0xFF) << 16) + (((int)(b[6]) & 0xFF) << 8) + ((int)(b[7])) & 0xFF;
				int timestamp = (((int)(b[8])) & 0xFF)*3600000 + (((int)(b[9])) & 0xFF)*600000 
						+ (((int)(b[10])) & 0xFF)*1000 + (((int)(b[4])) & 0xFF)*8;
				
				sample_data.setSignal_sample_(sample);
				sample_data.setTimestamp_(timestamp);
				sample_recevied = true;
			}
		}
	}
}
