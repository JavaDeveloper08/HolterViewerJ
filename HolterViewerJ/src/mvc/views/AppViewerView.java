package mvc.views;

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
import javax.swing.filechooser.FileNameExtensionFilter;

import data.*;
import adapters.Chart2DAdapter;
import mvc.*;
import mvc.models.AppViewerModel;

/**
 * @class AppViewerView
 * @brief class representing Viewer application views. Contains all GUI components and presented graphically AppModel class
 * @extends JFrame class
 */

public class AppViewerView extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	/** buttons */
	private JButton appViewerButtonOpenFile = new JButton("Open file");
	private	JButton appViewerButtonShow = new JButton("Show");
	private	JButton appViewerButtonClose = new JButton("Close");
	
	/** labels */
	private JLabel appViewerLabelInfo = new JLabel("[Exam information]");
	private JLabel appViewerLabelPatient = new JLabel();
	private JLabel appViewerLabelStartTime = new JLabel();
	private JLabel appViewerLabelStopTime = new JLabel();
	private JLabel appViewerLabelFrom = new JLabel("From:");
	private JLabel appViewerLabelTo = new JLabel("To:");
	
	/** spinners */
	private JSpinner appViewerSpinnerFrom = new JSpinner();
	private JSpinner appViewerSpinnerTo = new JSpinner();
	
	/** file chooser */
	private JFileChooser appFileChooser = new JFileChooser();
	
	/** chart */
	private int appViewerECGTraceMaxSize = 30000;
	private Chart2DAdapter appViewerECGChart = new Chart2DAdapter(appViewerECGTraceMaxSize);
	
	/** default constructors */
	public AppViewerView(){
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
		
		appViewerSpinnerFrom = Utils.createTimeSpinner(appViewerSpinnerFrom);
		appViewerSpinnerTo = Utils.createTimeSpinner(appViewerSpinnerTo);
		
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
	
	/**
	 * @methods setViewerFrameVisible()
	 * @brief visible viewer frame
	 */
	public void setViewerFrameVisible() {
		this.setVisible(true);	
	}
	
	/**
	 * Getters and setters to class fields
	 */	
	public JButton getAppViewerButtonClose() {
		return appViewerButtonClose;
	}
	
	
	/**
	 * @methods getSelectedPath()
	 * @brief get selected file path from file chooser
	 * @return file path
	 */
	public String getSelectedPath() {
		appFileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
		appFileChooser.setDialogTitle("Choose a file");
		int returnValue = appFileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION){
			 return appFileChooser.getSelectedFile().getPath();
		}
		return null;
	}

	/**
	 * @methods setInfo()
	 * @brief set exam information to view: patient, start and stop exam time
	 * @param patient data
	 * @param start exam time
	 * @param stop exam time
	 */
	public void setInfo (Patient p, Time start_time, Time stop_time) {
		String tmp = "Patient: " + p.getName_() + " " + p.getLast_name_() + " " + p.getID_num_();
		appViewerLabelPatient.setText(tmp);
		tmp = "Exam start: " + start_time.getFullTime();
		appViewerLabelStartTime.setText(tmp);
		tmp = "Exam finished: " + stop_time.getFullTime();
		appViewerLabelStopTime.setText(tmp);
		
		setSpinnerFrom(start_time);
		
		Time firstEndTime = new Time();
		firstEndTime.setHour_(start_time.getHour_());
		firstEndTime.setMinute_(start_time.getMinute_()+1);
		firstEndTime.setSecond_(0);
		
		setSpinnerTo(firstEndTime);
	}
	
	/**
	 * @methods setSpinnerFrom()
	 * @brief set time to spinner start time
	 * @param time to set
	 */
	public void setSpinnerFrom (Time time) {
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour_());
        calendar.set(Calendar.MINUTE, time.getMinute_());
        calendar.set(Calendar.SECOND, time.getSecond_());
        
		appViewerSpinnerFrom.getModel().setValue(calendar.getTime());
	}
	
	/**
	 * @methods getTimeSpinnerFrom()
	 * @brief get time from spinner start time
	 * @return set time
	 */
	public Time getTimeSpinnerFrom() {
		Time timeValue = new Time();
		java.util.Date time = new Date(0);
		
		time = (java.util.Date) appViewerSpinnerFrom.getModel().getValue();
		timeValue.setHour_(time.getHours());
		timeValue.setMinute_(time.getMinutes());
		timeValue.setSecond_(time.getSeconds());
		
		return timeValue;
	}
	
	/**
	 * @methods setSpinnerTo()
	 * @brief set time to spinner stop time
	 * @param time to set
	 */
	public void setSpinnerTo (Time time) {
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour_());
        calendar.set(Calendar.MINUTE, time.getMinute_());
        calendar.set(Calendar.SECOND, time.getSecond_());
        
		appViewerSpinnerTo.getModel().setValue(calendar.getTime());
	}
	
	/**
	 * @methods getTimeSpinnerTo()
	 * @brief get time from spinner stop time
	 * @return set time
	 */
	public Time getTimeSpinnerTo() {
		Time timeValue = new Time();
		java.util.Date time = new Date(0);
		
		time = (java.util.Date) appViewerSpinnerTo.getModel().getValue();
		timeValue.setHour_(time.getHours());
		timeValue.setMinute_(time.getMinutes());
		timeValue.setSecond_(time.getSeconds());
		
		return timeValue;
	}
	
	/**
	 * @methods upgradeChart()
	 * @brief set signal data to chart view
	 * @param vector of signal
	 */
	public void upgradeChart(Vector<Sample> signal) {
		appViewerECGChart.clearTraces();
		for (Sample i:signal){
			appViewerECGChart.addPoint(i.getSignal_sample_(),AppViewerModel.getxInterval());
		}
	}

	/**
	 * @methods clearChart()
	 * @brief clear chart view
	 */
	public void clearChart(){
		appViewerECGChart.clearTraces();
	}
	
	/**
	 * @methods clearViewer()
	 * @brief clear view of application
	 */
	public void clearViewer(){
		appViewerLabelPatient.setText("");
		appViewerLabelStartTime.setText("");
		appViewerLabelStopTime.setText("");
		
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        appViewerSpinnerFrom.getModel().setValue(calendar.getTime());
        appViewerSpinnerTo.getModel().setValue(calendar.getTime());
	}
	
	/**
	 * @methods setController()
	 * @brief set controller to class components
	 * @param application controller
	 */
	public void setController(AppController c) {
		appViewerButtonOpenFile.addActionListener(c);
		appViewerButtonShow.addActionListener(c);
		appViewerButtonClose.addActionListener(c);
	}
}
