package parsers;

import data.*;

public class AppDataParser {
	private Time time_data;
	private Sample sample_data;
	private static final byte[] header_tab = new byte[] {(byte)(0xA5), 0x5A, (byte)(0xFE)};
	private byte[] header_check = new byte[3];
	private static final int data_frame_length = 7;
	
	public AppDataParser(){
		header_check[0] = 0;
		header_check[1] = 0;
		header_check[2] = 0;
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
	
	public void clean_header_check_tab (){
		header_check[0] = 0;
		header_check[1] = 0;
		header_check[2] = 0;
	}
	
	public boolean is_header(byte b){
		header_check[0] = header_check[1];
		header_check[1] = header_check[2];
		header_check[2] = b;
		if(header_check.equals(header_tab))
			return true;
		
		return false;
	}
	
	public void parse(byte [] b){
		if(b.length != data_frame_length)
			return;
		if(b[0] == 1){
			time_data.setDay_(b[1]);
			time_data.setMonth_(b[2]);
			int year = (b[3] << 8) + b[4];
			time_data.setYear_(year);
			time_data.setHour_(b[5]);
			time_data.setMinute_(b[6]);
			time_data.setSecond_(b[7]);
		}
		else if(b[0] == 0){
			int sample = (b[2] << 16) + (b[3] << 8) + b[4];
			int timestamp = b[5]*3600000 + b[6]*600000 + b[7]*1000 + b[1]*8;
			
			sample_data.setSignal_sample_(sample);
			sample_data.setTimestamp_(timestamp);
		}
	}
}
