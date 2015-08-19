package mvc.views;

import data.*;

import java.awt.*;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.*;

import mvc.AppController;
import mvc.AppException;
import mvc.Utils;
import adapters.Chart2DAdapter;

public class AppMainView extends JFrame{

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
	private JSplitPane MainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private JPanel appLeftPanel = new JPanel();
	private JSplitPane appLeftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JPanel appRightPanel = new JPanel();
	private JPanel appPatientPanel = new JPanel();
	private JPanel appActionPanel = new JPanel();
	private JPanel appComPortPanel = new JPanel();
	private JPanel appCommandPanel = new JPanel();
	private JPanel appDownloadDataPanel = new JPanel();
	private JPanel appStatePanel = new JPanel();
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
	private JToggleButton appButtonDataLoad = new JToggleButton("Download");
	private JToggleButton appButtonDataStream = new JToggleButton("Stream data");
	private JToggleButton appButtonDataSave = new JToggleButton("Save data");
	private JButton appButtonDataErase = new JButton("Erase data");
	private JButton appButtonSendTime = new JButton("Time send");
	
	/** state Panel */
	/* button */
	private JButton appButtonGetState = new JButton("Get state");
	private JCheckBox appCheckBoxRun = new JCheckBox("Run",false);
	private JCheckBox appCheckBoxStream = new JCheckBox("Stream",false);
	private JCheckBox appCheckBoxSave = new JCheckBox("Save",false);
	
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
	private int appECGTraceMaxSize = 5000;
	private Chart2DAdapter appECGChart = new Chart2DAdapter(appECGTraceMaxSize); 
	
	/** default constructors (all views set) */
	public AppMainView(){
		/** set main view */
		this.setTitle("Holter ADS1292 v1.0");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(1200, 720)); 
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(new GridBagLayout());

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
		appStatePanel.setBorder(BorderFactory.createTitledBorder("Device state"));
		appCommandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
		appDownloadDataPanel.setBorder(BorderFactory.createTitledBorder("Download"));
		appPreviewPanel.setBorder(BorderFactory.createTitledBorder("ECG signal"));
		
		MainPanel.setDividerLocation(400);
		MainPanel.setEnabled(false);
	    MainPanel.setLeftComponent(appLeftPanel);  
	    MainPanel.setRightComponent(appRightPanel); 
	    GridBagConstraints gridBagConstraints = new GridBagConstraints(); 
	    gridBagConstraints.fill = GridBagConstraints.BOTH; 
	    gridBagConstraints.weightx = 1.0; 
	    gridBagConstraints.weighty = 1.0; 
	    getContentPane().add(MainPanel, gridBagConstraints);
		
	    appLeftPanel.setLayout(new GridBagLayout());
	    
	    appLeftSplitPane.setDividerLocation(240);
	    appLeftSplitPane.setEnabled(false);
	    appLeftSplitPane.setTopComponent(appPatientPanel);  
	    appLeftSplitPane.setBottomComponent(appActionPanel); 
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints(); 
	    gridBagConstraints2.fill = GridBagConstraints.BOTH; 
	    gridBagConstraints2.weightx = 1.0; 
	    gridBagConstraints2.weighty = 1.0;
	    appLeftPanel.add(appLeftSplitPane, gridBagConstraints2);
		
		GridLayout appRightPanelLayout = new GridLayout(1,1);
		appRightPanel.setLayout(appRightPanelLayout);
		appRightPanel.add(appPreviewPanel);
		
		/** set action view panel */
		appActionPanel.setLayout(new BorderLayout());
		
		JPanel appActionDownPanel = new JPanel(new BorderLayout());
		
		/* download view panel  */
		appDownloadDataPanel.setLayout(new BoxLayout(appDownloadDataPanel, BoxLayout.Y_AXIS));
		
		JPanel DOrow1 = new JPanel(new FlowLayout());
		JPanel DOrow2 = new JPanel(new FlowLayout());
		JPanel DOrow3 = new JPanel(new FlowLayout());
		
		DOrow1.add(appButtonDataLoad);
		DOrow2.add(Utils.createLabelTextFieldPanel(appLabelFileName, appTextFileName, 30));
		appProgressBar.setMinimum(0);
		appProgressBar.setMaximum(100);
		appProgressBar.setValue(appProgressBar.getMinimum());
		appProgressBar.setStringPainted(true);
		DOrow3.add(appProgressBar);
		
		appDownloadDataPanel.add(DOrow2);
		appDownloadDataPanel.add(DOrow1);
		appDownloadDataPanel.add(DOrow3);
		
		/* state view panel */
		appStatePanel.setLayout(new BoxLayout(appStatePanel, BoxLayout.X_AXIS));
		JPanel Srow1 = new JPanel(new FlowLayout());
		JPanel Srow2 = new JPanel(new FlowLayout());
		JPanel Srow3 = new JPanel(new FlowLayout());
		
		appCheckBoxRun.setEnabled(false);
		appCheckBoxSave.setEnabled(false);
		appCheckBoxStream.setEnabled(false);
		Srow1.add(appCheckBoxRun);
		Srow2.add(appCheckBoxSave);
		Srow3.add(appCheckBoxStream);
		
		appStatePanel.add(Srow1);
		appStatePanel.add(Srow2);
		appStatePanel.add(Srow3);
		
		appActionDownPanel.add(appStatePanel, BorderLayout.PAGE_START);
		appActionDownPanel.add(appDownloadDataPanel);
		appActionPanel.add(appActionDownPanel, BorderLayout.PAGE_END);
		
		JPanel appActionUpPanel = new JPanel(new GridLayout(1,2));
		
		appActionUpPanel.add(appComPortPanel);
		appActionUpPanel.add(appCommandPanel);
		
		/* com port view panel */
		appComPortPanel.setLayout(new BoxLayout(appComPortPanel, BoxLayout.Y_AXIS));

		JPanel CProw1 = new JPanel(new FlowLayout());
		JPanel CProw2 = new JPanel(new FlowLayout());
		JPanel CProw3 = new JPanel(new FlowLayout());
		JPanel CProw4 = new JPanel(new FlowLayout());
		
		CProw1.add(appButtonScanPort);
		CProw1.add(appComboBoxPortName);

		appLabelPortOpen.setVisible(false);
		CProw2.add(appLabelPortOpen);
		
		CProw3.add(appButtonOpenPort);
		appButtonClosePort.setEnabled(false);
		CProw4.add(appButtonClosePort);
		
		appComPortPanel.add(CProw1);
		appComPortPanel.add(CProw2);
		appComPortPanel.add(CProw3);
		appComPortPanel.add(CProw4);
		
		/* command view panel */
		appCommandPanel.setLayout(new BoxLayout(appCommandPanel, BoxLayout.Y_AXIS));
		JPanel Crow1 = new JPanel(new FlowLayout());
		JPanel Crow2 = new JPanel(new FlowLayout());
		JPanel Crow3 = new JPanel(new FlowLayout());
		JPanel Crow4 = new JPanel(new FlowLayout());
		JPanel Crow5 = new JPanel(new FlowLayout());
		
		Crow1.add(appButtonDataStream);
		Crow2.add(appButtonDataSave);
		Crow3.add(appButtonDataErase);
		Crow4.add(appButtonSendTime);
		Crow5.add(appButtonGetState);
		
		appCommandPanel.add(Crow1);
		appCommandPanel.add(Crow2);
		appCommandPanel.add(Crow3);
		appCommandPanel.add(Crow4);
		appCommandPanel.add(Crow5);
		
		appActionPanel.add(appActionUpPanel);
		
		/** set patient view panel */
		appPatientPanel.setLayout(new BorderLayout());
		
		JPanel appPatientDataPanel = new JPanel();
		appPatientDataPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints d = new GridBagConstraints();
		d.insets = new Insets(15, 5, 15, 5);
		d.gridx = 0;
		d.gridy = 0;
		d.ipady = 10;
		appPatientDataPanel.add(appLabelName, d);
		d.gridx = 1;
		d.gridy = 0;
		d.ipady = 10;
		appPatientDataPanel.add(appTextFieldName, d);
		d.gridx = 0;
		d.gridy = 1;
		d.ipady = 10;
		appPatientDataPanel.add(appLabelSurname, d);
		d.gridx = 1;
		d.gridy = 1;
		d.ipady = 10;
		appPatientDataPanel.add(appTextFieldSurname, d);
		d.gridx = 0;
		d.gridy = 2;
		d.ipady = 10;
		appPatientDataPanel.add(appLabelID, d);
		d.gridx = 1;
		d.gridy = 2;
		d.ipady = 10;
		appPatientDataPanel.add(appTextFieldID, d);
		
		appPatientPanel.add(appPatientDataPanel, BorderLayout.CENTER);
		
		JPanel appActionBar1 = new JPanel();
		appActionBar1.setLayout(new BoxLayout(appActionBar1, BoxLayout.X_AXIS));
		JPanel ABrow = new JPanel(new FlowLayout());
		ABrow.add(appButtonPatientClear);
		appActionBar1.add(ABrow);
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
		appECGChart.setYLabel("Signal[mV]");
		appECGChart.setGrid(true);
		
		appECGChart.createNewTraceLtd(Color.BLUE, "ECG Signal");

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
	
	/** STATE VIEW */
	public void set_run_state (boolean state){
		appCheckBoxRun.setSelected(state);
		appCheckBoxRun.setEnabled(state);
	}
	
	public void set_save_state (boolean state){
		appCheckBoxSave.setSelected(state);
		appCheckBoxSave.setEnabled(state);
		appButtonDataSave.setSelected(state);
	}
	
	public void set_stream_state (boolean state){
		appCheckBoxStream.setSelected(state);
		appCheckBoxStream.setEnabled(state);
		appButtonDataStream.setSelected(state);
	}
	
	/** DOWNLOAD VIEW */
	public void set_download_state (boolean state){
		appButtonDataLoad.setSelected(state);
	}
	
	public String getFileName() throws AppException {
		if(appTextFileName.getText().isEmpty())
			throw new AppException("Podaj nazwê docelowego pliku");
		return appTextFileName.getText();
	}
	
	public void setProgressBarValue (int value) {
		if (value > 100)
			appProgressBar.setValue(100);
		else if (value < 0)
			appProgressBar.setValue(100);
		else
			appProgressBar.setValue(value);
	}
	
	/** PREVIEW VIEW */
	public void setDateView(Time date){
		appLabelPreviewDate.setText(date.getYear_() + "/" + date.getMonth_() + "/" + date.getDay_());
	}
	
	public void setTimeView(Time date){
		appLabelPreviewTime.setText(date.getHour_() + "/" + date.getMinute_() + "/" +date.getSecond_());
	}
	
	public void addSampleToChart(Sample s){
		appECGChart.addPoint(s.getSignal_sample_(),2);
	}
	
	/** getters */
	public JToggleButton getAppButtonDataLoad() {
		return appButtonDataLoad;
	}

	public JToggleButton getAppButtonDataStream() {
		return appButtonDataStream;
	}

	public JToggleButton getAppButtonDataSave() {
		return appButtonDataSave;
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
		appButtonDataLoad.addActionListener(c);
		appButtonDataErase.addActionListener(c);
		appButtonSendTime.addActionListener(c);
		appButtonGetState.addActionListener(c);
	}
}
