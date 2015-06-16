package mvc;

import data.*;

import java.util.*;
import java.awt.*;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

import adapters.SimpleChartAdapter;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.views.ChartPanel;

public class AppView extends JFrame{

	private static final long serialVersionUID = 1L;

	/** menu view components */
	private JMenuBar appMenuBar = new JMenuBar();
	private JMenu appMenu = new JMenu("Menu");
	private JMenuItem appMenuClose = new JMenuItem("Close");
	private JMenuItem appMenuAbout = new JMenuItem("About");
	private JMenuItem appMenuViewer = new JMenuItem("Viewer");
	
	/** about frame */
	private JFrame appAboutFrame = new JFrame("About");
	private StyleContext appAboutStyleContext = new StyleContext();
	private DefaultStyledDocument appAboutStyleDoc = new DefaultStyledDocument(appAboutStyleContext);
	private JTextPane appAboutTextPane = new JTextPane(appAboutStyleDoc);
	//TODO dokoñczyæ about
	public static final String appAboutText = "HolterViewer\n";
	
	/** main window panels */
	private JPanel appLeftPanel = new JPanel();
	private JPanel appRightPanel = new JPanel();
	private JPanel appPatientPanel = new JPanel();
	private JPanel appActionPanel = new JPanel();
	private JPanel appComPortPanel = new JPanel();
	private JPanel appCommandPanel = new JPanel();
	private JPanel appDownloadDataPanel = new JPanel();
	private JPanel appPreviewPanel = new JPanel();
	
	/** patient panel components */
	/* labels */
	private JLabel appLabelName = new JLabel("Name:");
	private JLabel appLabelSurname = new JLabel("Surname:");
	private JLabel appLabelID = new JLabel("ID:");
	/* text fields */
	private JTextField appTextFieldName = new JTextField(15);
	private JTextField appTextFieldSurname = new JTextField(15);
	private JTextField appTextFieldID = new JTextField(15);
	/* action button */
	private JButton appButtonPatientClear = new JButton("Clear");
	
	/** action panel components */
	/** port com panel */
	/* buttons */
	private JButton appButtonScanPort = new JButton("Scan");
	private JButton appButtonOpenPort = new JButton("Open Port");
	private JButton appButtonClosePort = new JButton("Close Port");
	private JLabel appLabelPortOpen = new JLabel("Port open");
	/* combo box */
	private JComboBox<String> appComboBoxPortName = new JComboBox<String>();
	
	/** command panel */
	/* buttons */
	private JButton appButtonLoadData = new JButton("Download");
	private JButton appButtonDataStream = new JButton("Stream data");
	private JButton appButtonDataSave = new JButton("Save data");
	
	/** download panel */
	/*labels */
	private JLabel appLabelFileName = new JLabel("File name:");
	/* text fields */
	private JTextField appTextFileName = new JTextField(15);
	private JProgressBar appProgressBar = new JProgressBar();
	
	/** preview panel */
	/* labels */
	private JLabel appLabelPreviewDate = new JLabel();
	private JLabel appLabelPreviewTime = new JLabel();
	/* chart */
	private int appECGTraceMaxSize = 500;
	private SimpleChartAdapter appECGChart = new SimpleChartAdapter(appECGTraceMaxSize); 
	
	/** default constructors (all views set) */
	public AppView(){
		/** set main view */
		this.setTitle("Holter Viewer v1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1200,600);
		this.setResizable(true);
		this.setLocationRelativeTo(null);

		/** set menu view */
		appMenuBar.add(appMenu);
		appMenu.add(appMenuAbout);
		appMenu.add(appMenuViewer);
		appMenu.add(appMenuClose);
		setJMenuBar(appMenuBar);
		
		/** set main view panels */
		appPatientPanel.setBorder(BorderFactory.createTitledBorder("Patient"));
		appActionPanel.setBorder(BorderFactory.createTitledBorder("Action"));
		appComPortPanel.setBorder(BorderFactory.createTitledBorder("PortCOM"));
		appCommandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
		appDownloadDataPanel.setBorder(BorderFactory.createTitledBorder("Download"));
		appPreviewPanel.setBorder(BorderFactory.createTitledBorder("ECG signal"));
		
		//TODO add split!!!
		GridLayout appPanelsLayout = new GridLayout(1,2);
		this.setLayout(appPanelsLayout);
		this.add(appLeftPanel);
		this.add(appRightPanel);
		
		GridLayout appLeftPanelLayout = new GridLayout(2,1);
		appLeftPanel.setLayout(appLeftPanelLayout);
		appLeftPanel.add(appPatientPanel);
		appLeftPanel.add(appActionPanel);
		
		GridLayout appRightPanelLayout = new GridLayout(1,1);
		appRightPanel.setLayout(appRightPanelLayout);
		appRightPanel.add(appPreviewPanel);
		
		/** set action view panel */
		appActionPanel.setLayout(new BorderLayout());
		
		/* download view panel  */
		appDownloadDataPanel.setLayout(new BoxLayout(appDownloadDataPanel, BoxLayout.Y_AXIS));
		
		JPanel DOrow1 = new JPanel(new FlowLayout());
		JPanel DOrow2 = new JPanel(new FlowLayout());
		
		DOrow1.add(Utils.createLabelTextFieldPanel(appLabelFileName, appTextFileName, 30));
		DOrow2.add(appProgressBar);
		
		appDownloadDataPanel.add(DOrow1);
		appDownloadDataPanel.add(DOrow2);
		appActionPanel.add(appDownloadDataPanel, BorderLayout.PAGE_END);
		
		JPanel appActionRowPanel = new JPanel(new GridLayout(1,2));
		
		appActionRowPanel.add(appComPortPanel);
		appActionRowPanel.add(appCommandPanel);
		
		/* com port view panel */
		appComPortPanel.setLayout(new BoxLayout(appComPortPanel, BoxLayout.Y_AXIS));

		JPanel CProw1 = new JPanel(new FlowLayout());
		JPanel CProw2 = new JPanel(new FlowLayout());
		JPanel CProw3 = new JPanel(new FlowLayout());
		
		CProw1.add(appButtonScanPort);
		CProw1.add(appComboBoxPortName);

		appLabelPortOpen.setVisible(false);
		CProw2.add(appLabelPortOpen);
		
		CProw3.add(appButtonOpenPort);
		appButtonClosePort.setEnabled(false);
		CProw3.add(appButtonClosePort);
		
		appComPortPanel.add(CProw1);
		appComPortPanel.add(CProw2);
		appComPortPanel.add(CProw3);
		
		/* command view panel */
		appCommandPanel.setLayout(new BoxLayout(appCommandPanel, BoxLayout.Y_AXIS));
		JPanel Crow1 = new JPanel(new FlowLayout());
		JPanel Crow2 = new JPanel(new FlowLayout());
		JPanel Crow3 = new JPanel(new FlowLayout());
		
		Crow1.add(appButtonDataStream);
		Crow2.add(appButtonDataSave);
		Crow3.add(appButtonLoadData);
		
		appCommandPanel.add(Crow1);
		appCommandPanel.add(Crow2);
		appCommandPanel.add(Crow3);
		
		appActionPanel.add(appActionRowPanel);
		
		/** set patient view panel */
		appPatientPanel.setLayout(new BorderLayout());
		
		JPanel appPatientDataPanel = new JPanel();
		appPatientDataPanel.setLayout(new GridLayout(0,2,2,20));
		
		appPatientDataPanel.add(appLabelName);
		appPatientDataPanel.add(appTextFieldName);
		appPatientDataPanel.add(appLabelSurname);
		appPatientDataPanel.add(appTextFieldSurname);
		appPatientDataPanel.add(appLabelID);
		appPatientDataPanel.add(appTextFieldID);
		
		appPatientPanel.add(appPatientDataPanel, BorderLayout.CENTER);
		
		JPanel appActionBar1 = new JPanel();
		appActionBar1.setLayout(new BoxLayout(appActionBar1, BoxLayout.X_AXIS));
		//TODO add gap
		appActionBar1.add(appButtonPatientClear);
		appPatientPanel.add(appActionBar1, BorderLayout.PAGE_END);
		
		/** set preview view */
		appPreviewPanel.setLayout(new BorderLayout());
		JPanel appExaminationDatePanel = new JPanel(new GridLayout(1,3));
		JLabel appExaminationDateLabel = new JLabel("Examination date:");
		appExaminationDatePanel.add(appExaminationDateLabel);
		LocalDateTime defaultDate = LocalDateTime.now();
		appLabelPreviewDate.setText(defaultDate.getYear() + "/" + defaultDate.getMonthValue() + "/" + defaultDate.getDayOfMonth());
		appExaminationDatePanel.add(appLabelPreviewDate);
		appLabelPreviewTime.setText(defaultDate.getHour() + ":" + defaultDate.getMinute() + ":" + defaultDate.getSecond());
		appExaminationDatePanel.add(appLabelPreviewTime);
		appPreviewPanel.add(appExaminationDatePanel, BorderLayout.PAGE_END);
		
		appECGChart.setXLabel("Time[ms]");
		appECGChart.setYLabel("Signal[V]");
		appECGChart.setGrid(true);
		
		appECGChart.createNewTraceLtd(Color.RED, "ECG Signal");

		appPreviewPanel.add(appECGChart.getChartPanel(), BorderLayout.CENTER);
	}
	
	/** ABOUT FRAME */
	public JFrame setAboutFrame() {
		appAboutFrame.setSize(300,300);
		appAboutFrame.setResizable(true);
		appAboutFrame.setLocationRelativeTo(null);
		appAboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final Style heading2Style = appAboutStyleContext.addStyle("Heading2", null);
	    heading2Style.addAttribute(StyleConstants.Foreground, Color.BLACK);
	    heading2Style.addAttribute(StyleConstants.FontSize, new Integer(16));
	    heading2Style.addAttribute(StyleConstants.FontFamily, "serif");
	    heading2Style.addAttribute(StyleConstants.Bold, new Boolean(true));
	    heading2Style.addAttribute(StyleConstants.Alignment, Alignment.CENTER);
	    
		try{
			appAboutStyleDoc.insertString(0, appAboutText, null);
			appAboutStyleDoc.setParagraphAttributes(0, 1, heading2Style, false);
		} catch (Exception e) {
			System.out.println("Exception when constructing document: " + e);
		}
		
		appAboutFrame.getContentPane().add(new JScrollPane(appAboutTextPane));
		appAboutFrame.setVisible(true);
		
		return appAboutFrame;
	}
	
	/** PATIENT VIEW */
	
	/**
	 * @fn readPatientView()
	 * @brief read data from patient view
	 * @return patient object
	 * @throws AppException
	 */
	public Patient readPatientView() throws AppException {
		Patient newPatient = new Patient();
		if(Utils.isText(appTextFieldName.getText()))
			newPatient.setName_(appTextFieldName.getText());
		else {
			throw new AppException("Podano b³êdne imiê");
		}
		if(Utils.isText(appTextFieldSurname.getText()))
			newPatient.setLast_name_(appTextFieldSurname.getText());
		else {
			throw new AppException("Podano b³êdne nazwisko");
		}
		String id_num = appTextFieldID.getText();
		if(Utils.isNumber(id_num)){
			if(id_num.length() == 11)
				newPatient.setID_num_(id_num);
			else {
				throw new AppException("Podano za krótki numer PESEL");
			}
		}
		else {
			throw new AppException("Podano z³y numer PESEL");
		}
		
		return newPatient;
	}
	
	/**
	 * @fn cleanPatientView()
	 * @brief clean patient view
	 */
	public void cleanPatientView() {
		appTextFieldName.setText("");
		appTextFieldSurname.setText("");
		appTextFieldID.setText("");
	}
	
	/** ACTION VIEW */
	public void setPortNames(String[] tab) {
		appComboBoxPortName.removeAllItems();
		for (int i = 0; i < tab.length; i++) {
			appComboBoxPortName.addItem(tab[i]);
		}
	}
	
	public String getFileName() throws AppException {
		if(appTextFileName.getText().isEmpty())
			throw new AppException("Podaj nazwê docelowego pliku");
		return appTextFileName.getText();
	}
	
	public String getUserPort() {
		return (String) appComboBoxPortName.getSelectedItem();
	}
	
	public void openPortAction(){
		appButtonClosePort.setEnabled(true);
		appButtonOpenPort.setEnabled(false);
		appLabelPortOpen.setVisible(true);
	}
	
	public void closePortAction(){
		appButtonOpenPort.setEnabled(true);
		appButtonClosePort.setEnabled(false);
		appLabelPortOpen.setVisible(false);
	}
	
	/** PREVIEW VIEW */
	public void setDateView(Time date){
		appLabelPreviewDate.setText(date.getYear_() + "/" + Integer.toHexString(date.getMonth_()) + "/" + Integer.toHexString(date.getDay_()));
	}
	
	public void setTimeView(Time date){
		appLabelPreviewTime.setText(Integer.toHexString(date.getHour_()) + "/" + Integer.toHexString(date.getMinute_()) + "/" + Integer.toHexString(date.getSecond_()));
	}
	
	public void addSampleToChart(Sample s){
		appECGChart.addPoint(s.getSignal_sample_());
	}
	
	public void setController(AppController c) {
		appMenuAbout.addActionListener(c);
		appMenuClose.addActionListener(c);
		appMenuViewer.addActionListener(c);
		appButtonPatientClear.addActionListener(c);
		appButtonScanPort.addActionListener(c);
		appButtonOpenPort.addActionListener(c);
		appButtonClosePort.addActionListener(c);
		appButtonDataStream.addActionListener(c);
		appButtonDataSave.addActionListener(c);
		appButtonLoadData.addActionListener(c);
		}

}
