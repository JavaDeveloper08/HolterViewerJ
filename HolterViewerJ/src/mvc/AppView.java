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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.views.ChartPanel;

public class AppView extends JFrame{

	private static final long serialVersionUID = 1L;

	/** menu view components */
	private JMenuBar appMenuBar = new JMenuBar();
	private JMenu appMenu = new JMenu("Menu");
	private JMenuItem appMenuClose = new JMenuItem("Close");
	private JMenuItem appMenuAbout = new JMenuItem("About");
	
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
	private JPanel appDownloadPanel = new JPanel();
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
	
	/** download panel */
	/* buttons */
	private JButton appButtonScanPort = new JButton("Scan");
	private JButton appButtonOpenPort = new JButton("Open Port");
	private JButton appButtonClosePort = new JButton("Close Port");
	private JButton appButtonLoadData = new JButton("Download");
	/*labels */
	private JLabel appLabelFileName = new JLabel("File name:");
	/* text fields */
	private JTextField appTextFileName = new JTextField(15);
	/* combo box */
	private JComboBox<String> appComboBoxPortName = new JComboBox<String>();
	
	/** preview panel */
	/* labels */
	private JLabel appLabelPreviewDate = new JLabel();
	private JLabel appLabelPreviewTime = new JLabel();
	/* chart */
	private int appECGTraceMaxSize = 200;
	private Chart2D appECGChart = new Chart2D();
	private ITrace2D appECGTrace = new Trace2DLtd(appECGTraceMaxSize); 
	
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
		appMenu.add(appMenuClose);
		setJMenuBar(appMenuBar);
		
		/** set main view panels */
		appPatientPanel.setBorder(BorderFactory.createTitledBorder("Patient"));
		appDownloadPanel.setBorder(BorderFactory.createTitledBorder("Download data"));
		appPreviewPanel.setBorder(BorderFactory.createTitledBorder("ECG signal"));
		
		GridLayout appPanelsLayout = new GridLayout(1,2);
		this.setLayout(appPanelsLayout);
		this.add(appLeftPanel);
		this.add(appRightPanel);
		
		GridLayout appLeftPanelLayout = new GridLayout(2,1);
		appLeftPanel.setLayout(appLeftPanelLayout);
		appLeftPanel.add(appPatientPanel);
		appLeftPanel.add(appDownloadPanel);
		
		GridLayout appRightPanelLayout = new GridLayout(1,1);
		appRightPanel.setLayout(appRightPanelLayout);
		appRightPanel.add(appPreviewPanel);
		
		/** set download view panel */
		appDownloadPanel.setLayout(new BoxLayout(appDownloadPanel, BoxLayout.Y_AXIS));

		JPanel row1 = new JPanel(new FlowLayout());
		JPanel row2 = new JPanel(new FlowLayout());
		JPanel row3 = new JPanel(new FlowLayout());
		JPanel row4 = new JPanel(new FlowLayout());
		
		//appComboBoxPortName = new JComboBox<String>(this.communicator.scan());
		row1.add(appButtonScanPort);
		row1.add(appComboBoxPortName);

		row2.add(appButtonOpenPort);
		row2.add(appButtonClosePort);
		
		row3.add(appLabelFileName);
		row3.add(appTextFileName);
		
		row4.add(appButtonLoadData);
		
		appDownloadPanel.add(row1);
		appDownloadPanel.add(row2);
		appDownloadPanel.add(row3);
		appDownloadPanel.add(row4);
		
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
		appActionBar1.setLayout(new GridLayout(1,1));
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
		
		appECGChart.addTrace(appECGTrace);
		appECGChart.setBackground(Color.BLACK);
		appPreviewPanel.add(appECGChart, BorderLayout.CENTER);
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
	/*public Patient readPatientView() throws AppException {
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
		if(appRadioButtonMan.isSelected())
			newPatient.setSex_(false);
		else if(appRadioButtonWoman.isSelected())
			newPatient.setSex_(true);
		else {
			throw new AppException("Proszê wybraæ p³eæ");
		}
		newPatient.setInsurance_(appComboBoxInsurance.getSelectedIndex());
		return newPatient;
	}*/
	
	/**
	 * @fn cleanPatientView()
	 * @brief clean patient view
	 */
	public void cleanPatientView() {
		appTextFieldName.setText("");
		appTextFieldSurname.setText("");
		appTextFieldID.setText("");
	}
	
	/** DOWNLOAD VIEW */
	public void setPortNames(String[] tab) {
		appComboBoxPortName.removeAllItems();
		for (int i = 0; i < tab.length; i++) {
			appComboBoxPortName.addItem(tab[i]);
		}
	}
	
	public String getUserPort() {
		return (String) appComboBoxPortName.getSelectedItem();
	}
	
	/** PREVIEW VIEW */
	public void setDateView(Time date){
		appLabelPreviewDate.setText(date.getYear_() + "/" + date.getMonth_() + "/" + date.getDay_());
	}
	
	public void setTimeView(Time date){
		appLabelPreviewTime.setText(date.getHour_() + "/" + date.getMinute_() + "/" + date.getSecond_());
	}
	
	public void setController(AppController c) {
		appMenuAbout.addActionListener(c);
		appMenuClose.addActionListener(c);
		appButtonPatientClear.addActionListener(c);
		appButtonScanPort.addActionListener(c);
		appButtonOpenPort.addActionListener(c);
		appButtonLoadData.addActionListener(c);
	}

}
