package mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.sql.Date;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapters.FileAdapter;
import adapters.SimpleChartAdapter;
import data.*;

public class AppViewer extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private static final int sampling_rate = 500;
	
	/* buttons */
	private JButton appViewerButtonOpenFile = new JButton("Open file");
	private	JButton appViewerButtonShow = new JButton("Show");
	private	JButton appViewerButtonClose = new JButton("Close");
	/* labels */
	private JLabel appViewerLabelInfo = new JLabel("[Exam information]");
	private JLabel appViewerLabelPatient = new JLabel();
	private JLabel appViewerLabelStartTime = new JLabel();
	private JLabel appViewerLabelStopTime = new JLabel();
	
	private JLabel appViewerLabelFrom = new JLabel("From:");
	private JLabel appViewerLabelTo = new JLabel("To:");
	/* spinners */
	private JSpinner appViewerSpinnerFrom = new JSpinner();
	private JSpinner appViewerSpinnerTo = new JSpinner();
	/* time */
	private Time appViewerTimeFrom = new Time();
	private Time appViewerTimeTo = new Time();
	
	/* file chooser */
	private JFileChooser appFileChooser = new JFileChooser();
	public String pathName;
	/* chart */
	private int appViewerECGTraceMaxSize = 30000;
	private SimpleChartAdapter appViewerECGChart = new SimpleChartAdapter(appViewerECGTraceMaxSize);
	
	/* load file */
	private FileAdapter readFile;
	private Vector<Vector<Sample>> data_sample = new Vector<Vector<Sample>>();
	private String csvCellSeparator = ",";
	private String csvLineSeparator = System.lineSeparator();
	
	/*patient data */
	private Patient patient = new Patient();
	/* time data */
	private Time startExamTime = new Time();
	private Time stopExamTime = new Time();
	
	/** default constructors (all views set) */
	public AppViewer(){
		this.setTitle("Viewer Window");
		this.setSize(1100,620);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		appViewerECGChart.setXLabel("Time[ms]");
		appViewerECGChart.setYLabel("Signal[mV]");
		appViewerECGChart.setGrid(true);
		
		appViewerECGChart.createNewTraceLtd(Color.BLUE, "ECG Signal");
		this.add(appViewerECGChart.getChartPanel(), BorderLayout.CENTER);
		
		JPanel appViewerActionBar = new JPanel();
		appViewerActionBar.setLayout(new BoxLayout(appViewerActionBar, BoxLayout.X_AXIS));
		
		JPanel Vcolumn1 = new JPanel(new FlowLayout());
		JPanel Vcolumn2 = new JPanel(new FlowLayout());
		JPanel Vcolumn3 = new JPanel(new FlowLayout());
		JPanel Vcolumn4 = new JPanel(new FlowLayout());
		JPanel Vcolumn5 = new JPanel(new FlowLayout());
		
		appViewerSpinnerFrom = Utils.crateTimeSpinner(appViewerSpinnerFrom);
		appViewerSpinnerTo = Utils.crateTimeSpinner(appViewerSpinnerTo);
		
		Vcolumn1.add(appViewerButtonOpenFile);
		Vcolumn2.add(Utils.createLabelTextFieldPanel(appViewerLabelFrom, appViewerSpinnerFrom, 10));
		Vcolumn3.add(Utils.createLabelTextFieldPanel(appViewerLabelTo, appViewerSpinnerTo, 10));
		Vcolumn4.add(appViewerButtonShow);
		Vcolumn5.add(appViewerButtonClose);

		appViewerActionBar.add(Vcolumn1);
		appViewerActionBar.add(Vcolumn2);
		appViewerActionBar.add(Vcolumn3);
		appViewerActionBar.add(Vcolumn4);
		appViewerActionBar.add(Vcolumn5);
		
		this.add(appViewerActionBar, BorderLayout.PAGE_END);
		
		JPanel appViewerInfoBar = new JPanel();
		appViewerInfoBar.setLayout(new BoxLayout(appViewerInfoBar, BoxLayout.X_AXIS));
		
		JPanel Icolumn1 = new JPanel(new FlowLayout());
		JPanel Icolumn2 = new JPanel(new FlowLayout());
		JPanel Icolumn3 = new JPanel(new FlowLayout());
		JPanel Icolumn4 = new JPanel(new FlowLayout());
		
		Icolumn1.add(appViewerLabelInfo);
		Icolumn2.add(appViewerLabelPatient);
		Icolumn3.add(appViewerLabelStartTime);
		Icolumn4.add(appViewerLabelStopTime);

		appViewerInfoBar.add(Icolumn1);
		appViewerInfoBar.add(Icolumn2);
		appViewerInfoBar.add(Icolumn3);
		appViewerInfoBar.add(Icolumn4);
		
		this.add(appViewerInfoBar, BorderLayout.PAGE_START);
	}
	
	public void setViewerFrameVisible() {
		this.setVisible(true);	
	}
	
	/**
	 * @fn getSelectedPath()
	 * @brief get selected file path
	 * @return file path
	 */
	public String getSelectedPath() {
		appFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
		appFileChooser.setDialogTitle("Choose a file");
		int returnValue = appFileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION)
		{
			 pathName = appFileChooser.getSelectedFile().getPath();
		}
			 	
		return pathName;
	}

	public JButton getAppViewerButtonClose() {
		return appViewerButtonClose;
	}
	
	public void setController(AppController c) {
		appViewerButtonOpenFile.addActionListener(c);
		appViewerButtonShow.addActionListener(c);
		appViewerButtonClose.addActionListener(c);
	}
	
	private void parseHeader (String header) {
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
	
	private void parseTime(String line, int source) {
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
	
	private Sample parseSample (String line){
		Sample tmp = new Sample();
		String s_tmp;
		int head_index = 0;
		int tail_index = 0;
		
		tail_index = line.indexOf(',', head_index);
		s_tmp = line.substring(head_index,tail_index);
		
		tmp.setTimestamp_(Double.valueOf(s_tmp));
		
		head_index = tail_index + 1;
		s_tmp = line.substring(head_index,line.length());
		
		tmp.setSignal_sample_(Double.valueOf(s_tmp));
		
		return tmp;
	}
	
	private void setInfo (){
		String tmp = "Patient: " + patient.getName_() + " " + patient.getLast_name_() + " " + patient.getID_num_();
		appViewerLabelPatient.setText(tmp);
		tmp = "Exam start: " + startExamTime.getTime();
		appViewerLabelStartTime.setText(tmp);
		tmp = "Exam finished: " + stopExamTime.getTime();
		appViewerLabelStopTime.setText(tmp);
	}
	
	private void setSpinnerFrom (Time time){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour_());
        calendar.set(Calendar.MINUTE, time.getMinute_());
        calendar.set(Calendar.SECOND, time.getSecond_());
        
		appViewerSpinnerFrom.getModel().setValue(calendar.getTime());
	}
	
	public Time getTimeSpinnerFrom(){
		Time timeValue = new Time();
		java.util.Date time = new Date(0);
		
		time = (java.util.Date) appViewerSpinnerFrom.getModel().getValue();
		timeValue.setHour_(time.getHours());
		timeValue.setMinute_(time.getMinutes());
		timeValue.setSecond_(time.getSeconds());
		
		return timeValue;
	}
	
	
	private void setSpinnerTo (Time time){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour_());
        calendar.set(Calendar.MINUTE, time.getMinute_());
        calendar.set(Calendar.SECOND, time.getSecond_());
        
		appViewerSpinnerTo.getModel().setValue(calendar.getTime());
	}
	
	public Time getTimeSpinnerTo(){
		Time timeValue = new Time();
		java.util.Date time = new Date(0);
		
		time = (java.util.Date) appViewerSpinnerTo.getModel().getValue();
		timeValue.setHour_(time.getHours());
		timeValue.setMinute_(time.getMinutes());
		timeValue.setSecond_(time.getSeconds());
		
		return timeValue;
	}
	
	
	public void setAppViewerTimeFrom(Time appViewerTimeFrom) {
		this.appViewerTimeFrom = appViewerTimeFrom;
	}

	public void setAppViewerTimeTo(Time appViewerTimeTo) {
		this.appViewerTimeTo = appViewerTimeTo;
	}

	public void loadData(String file_path){
		readFile = new FileAdapter(file_path, csvCellSeparator, csvLineSeparator);
		readFile.openToRead();
		parseHeader(readFile.readLine());
		parseTime(readFile.readLine(),1);
		parseTime(readFile.readLine(),2);
		
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
				sample = parseSample(line);
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
		
		readFile.close();
		
		int seconds_num = 0;
		seconds_num = 60 - (data_sample.get(0).size()/sampling_rate);
		if(data_sample.get(0).size()/sampling_rate != 0)
			seconds_num--;
		
		startExamTime.setSecond_(seconds_num);
		
		seconds_num = data_sample.lastElement().size()/500;
		if(data_sample.get(0).size()/500 != 0)
			seconds_num++;
		
		stopExamTime.setSecond_(seconds_num);
		setInfo();
		
		appViewerECGChart.clearTraces();
		for (Sample i:data_sample.get(0)){
			appViewerECGChart.addPoint(i.getSignal_sample_());
		}
		
		setSpinnerFrom(startExamTime);
		
		Time firstEndTime = new Time();
		firstEndTime.setHour_(startExamTime.getHour_());
		firstEndTime.setMinute_(startExamTime.getMinute_()+1);
		firstEndTime.setSecond_(0);
		
		setSpinnerTo(firstEndTime);
	}
	
	//TODO obs³uga b³êdów
	public void upgradeChart () {
		
		if((Utils.timeDiff(appViewerTimeTo, appViewerTimeFrom) > 60) || (Utils.timeDiff(appViewerTimeTo, appViewerTimeFrom) < 0))
			return;
		
		appViewerECGChart.clearTraces();
		if(appViewerTimeTo.getMinute_() == appViewerTimeFrom.getMinute_()){
			int exam_minute = Utils.timeDiffWithoutSec(appViewerTimeFrom, startExamTime) / 60;
			if(exam_minute == 0) {
				int start = (appViewerTimeFrom.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				int stop = (appViewerTimeTo.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute).get(i).getSignal_sample_());
				}
			}
			else if(exam_minute == data_sample.size()-1){
				int start = appViewerTimeFrom.getSecond_()*sampling_rate;
				int stop = (appViewerTimeTo.getSecond_() - appViewerTimeFrom.getSecond_())*sampling_rate;
				if (stop > data_sample.get(exam_minute).size())
					stop = data_sample.get(exam_minute).size();
				
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute).get(i).getSignal_sample_());
				}
			}
			else {
				for(int i = appViewerTimeFrom.getSecond_()*500; i<appViewerTimeTo.getSecond_()*500; i++)
					appViewerECGChart.addPoint(data_sample.get(exam_minute).get(i).getSignal_sample_());
			}
		}
		else {
			int exam_minute_start = Utils.timeDiffWithoutSec(appViewerTimeFrom, startExamTime) / 60;
			int exam_minute_stop = exam_minute_start + 1;
			
			if(exam_minute_start == 0) {
				int start = (appViewerTimeFrom.getSecond_() - startExamTime.getSecond_())*sampling_rate;
				int stop = data_sample.get(exam_minute_start).size();
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute_start).get(i).getSignal_sample_());
				}
			}
			else {
				int start = appViewerTimeFrom.getSecond_()*sampling_rate;
				int stop = data_sample.get(exam_minute_start).size();
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute_start).get(i).getSignal_sample_());
				}
			}
			
			if(exam_minute_stop == data_sample.size()-1) {
				int start = 0;
				int stop = appViewerTimeTo.getSecond_()*sampling_rate;
				if (stop > data_sample.get(exam_minute_stop).size())
					stop = data_sample.get(exam_minute_stop).size();
				
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute_stop).get(i).getSignal_sample_());
				}
			}
			else {
				int start = 0;
				int stop = appViewerTimeTo.getSecond_()*sampling_rate;
				for(int i = start; i<stop; i++){
					appViewerECGChart.addPoint(data_sample.get(exam_minute_start).get(i).getSignal_sample_());
				}
			}
			
		}
	}

	public void clearChart(){
		appViewerECGChart.clearTraces();
	}
}
