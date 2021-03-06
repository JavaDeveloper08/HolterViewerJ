package mvc.models;

import java.util.Vector;

import data.*;
import adapters.FileAdapter;
import parsers.FileDataParser;

import mvc.AppController;
import mvc.AppException;
import mvc.Utils;

/**
 * @class AppViewerModel
 * @brief class representing all Viewer application date, application state and allows access to them
 */

public class AppViewerModel {
	/** Global variables */
	private static final int sampling_rate = 500;
	private static final int x_interval = 1000/sampling_rate;
	
	/** Controller*/
	private AppController controller;
	
	/** Data */
	private Patient patient;
	private Vector<Vector<Sample>> data_sample;
	private Vector<Sample> data_view_sample;
	
	private Time startExamTime;
	private Time stopExamTime;
	private Time TimeFrom;
	private Time TimeTo;
	
	/** COMMUNICATION */
	/* File */
	private FileAdapter readFile;
	private String csvCellSeparator = ",";
	private String csvLineSeparator = System.lineSeparator();
	
	/* Parser */
	private FileDataParser dataParser;
	
	/** default constructors */
	public AppViewerModel(){
		controller = null;
		patient = new Patient();
		data_sample = new Vector<Vector<Sample>>();
		data_view_sample = new Vector<Sample>();
		startExamTime = new Time();
		stopExamTime = new Time();
		TimeFrom = new Time();
		TimeTo = new Time();
		readFile = new FileAdapter();
		dataParser = new FileDataParser();
	}
	
	/**
	 * Getters and setters to class fields
	 */
	public void setController(AppController c){
		this.controller = c;
	}
	
	public static int getSamplingRate() {
		return sampling_rate;
	}

	public static int getxInterval() {
		return x_interval;
	}
	
	public Patient getPatient() {
		return patient;
	}

	public Time getStartExamTime() {
		return startExamTime;
	}

	public Time getStopExamTime() {
		return stopExamTime;
	}
	
	public Vector<Sample> getData_view_sample() {
		return data_view_sample;
	}

	/**
	 * @methods loadData()
	 * @brief load data from file to program
	 * @param path to load file
	 * @throws AppException
	 */
	public void loadData(String file_path)  throws AppException{
		readFile = new FileAdapter(file_path, csvCellSeparator, csvLineSeparator);
		try{
			readFile.openToRead();
			
			dataParser.parseHeader(readFile.readLine());
			patient = dataParser.getPatient();
			
			dataParser.parseTime(readFile.readLine(),1);
			startExamTime = dataParser.getStartExamTime();
			
			dataParser.parseTime(readFile.readLine(),2);
			stopExamTime = dataParser.getStopExamTime();
			
			String line = "";
			Boolean data_end = false;
			Sample sample = new Sample();
			Vector<Sample> s_vector= new Vector<Sample>();
			double current_timestamp = startExamTime.getHour_()*60 + startExamTime.getMinute_();
			while(!data_end){
				line = readFile.readLine();
				
				if(line.equals("END")){
					data_end = true; 
					if(!s_vector.isEmpty()){
						Vector<Sample> tmp = new Vector<Sample>(s_vector);
						data_sample.add(tmp);
						s_vector.clear();
					}
				}
				else {
					sample = dataParser.parseSample(line);
					if(sample.getTimestamp_() == current_timestamp){
						s_vector.add(sample);
					}
					else {
						if(!s_vector.isEmpty()){
							Vector<Sample> tmp = new Vector<Sample>(s_vector);
							data_sample.add(tmp);
							current_timestamp = sample.getTimestamp_();
							s_vector.clear();
							s_vector.add(sample);
						}
					}
				}
			}
		} catch (AppException exception){
			exception.show_exception(); 
		}
		
		readFile.close();
		
		int seconds_num = 0;
		seconds_num = 60 - (data_sample.get(0).size()/sampling_rate);
		if(data_sample.get(0).size()/sampling_rate != 0)
			seconds_num--;
		
		startExamTime.setSecond_(seconds_num);
		
		seconds_num = data_sample.lastElement().size()/sampling_rate;
		if(data_sample.get(0).size()/sampling_rate != 0)
			seconds_num++;
		
		stopExamTime.setSecond_(seconds_num);
		
		if(!data_sample.isEmpty()){
			data_view_sample.clear();
			for (Sample i:data_sample.get(0)){
				data_view_sample.add(i);
			}
		}
		else
			throw new AppException("Brak danych w pliku");
	}
	
	/**
	 * @methods findSignalSection()
	 * @brief find the section of the signal from time 1 to time 2
	 * @param from time - start signal
	 * @param to_time - stop signal
	 * @return vector of section of the signal
	 * @throws AppException
	 */
	public Vector<Sample> findSignalSection (Time from_time, Time to_time) throws AppException{
		TimeFrom = from_time;
		TimeTo = to_time;
		data_view_sample.clear();
		
		if((Utils.timeDiffSec(TimeTo, TimeFrom) > 60) || (Utils.timeDiffSec(TimeTo, TimeFrom) < 0))
			throw new AppException ("Podanno b��dy czas sygna�u (0-1min)");
		
		if(data_sample.isEmpty())
			throw new AppException("Brak danych do wy�wietlenia");
		
		if(TimeTo.getMinute_() == TimeFrom.getMinute_()){
			int exam_minute = Utils.timeDiffMin(TimeFrom, startExamTime) / 60;
			if(exam_minute == 0) {
				int start = (TimeFrom.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				int stop = (TimeTo.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute).get(i));
				}
			}
			else if(exam_minute == data_sample.size()-1){
				int start = TimeFrom.getSecond_()*sampling_rate;
				int stop = (TimeTo.getSecond_() - TimeFrom.getSecond_())*sampling_rate;
				if (stop > data_sample.get(exam_minute).size())
					stop = data_sample.get(exam_minute).size();
				
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute).get(i));
				}
			}
			else {
				for(int i = TimeFrom.getSecond_()*sampling_rate; i<TimeTo.getSecond_()*sampling_rate; i++)
					data_view_sample.add(data_sample.get(exam_minute).get(i));
			}
		}
		else {
			int exam_minute_start = Utils.timeDiffMin(TimeFrom, startExamTime) / 60;
			int exam_minute_stop = exam_minute_start + 1;
			
			if(exam_minute_start == 0) {
				int start = (TimeFrom.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				int stop = data_sample.get(exam_minute_start).size();
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute_start).get(i));
				}
			}
			else {
				int start = TimeFrom.getSecond_()*sampling_rate;
				int stop = data_sample.get(exam_minute_start).size();
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute_start).get(i));
				}
			}
			
			if(exam_minute_stop == data_sample.size()-1) {
				int start = 0;
				int stop = TimeTo.getSecond_()*sampling_rate;
				if (stop > data_sample.get(exam_minute_stop).size())
					stop = data_sample.get(exam_minute_stop).size();
				
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute_stop).get(i));
				}
			}
			else {
				int start = 0;
				int stop = TimeTo.getSecond_()*sampling_rate;
				for(int i = start; i<stop; i++){
					data_view_sample.add(data_sample.get(exam_minute_stop).get(i));
				}
			}
			
		}
		return data_view_sample;
	}
	
	/**
	 * @methods dataClear()
	 * @brief clear signal data objects
	 */
	public void dataClear() {
		data_sample.clear();
		data_view_sample.clear();
	}	
}
