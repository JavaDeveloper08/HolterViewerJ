package mvc;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

import data.*;

/**
 * @class Utils
 * @brief class contains static auxiliary methods for application
 */

public class Utils {
	
	/**
	 * @methods isText()
	 * @brief check if string line contain text
	 * @param string line
	 * @return true if line contain text
	 */
	public static boolean isText (String line){
		if(line.isEmpty())
			return false;
		for(int i=0; i<line.length(); i++){
			if(!(Character.isLetter(line.charAt(i))))
				if(Character.compare(line.charAt(i), '-') ==0 && i!=0) {
					;
				}
				else
					return false;
		}
		return true;
	}
	
	/**
	 * @methods isNumber()
	 * @brief check if string line contain number
	 * @param string line
	 * @return true if line contain number
	 */
	public static boolean isNumber (String line){
		if(line.isEmpty())
			return false;
		for(int i=0; i<line.length(); i++){
			if(!(Character.isDigit(line.charAt(i))))
					return false;
		}
		return true;
	}
	
	/**
	 * @methods createLabelTextFieldPanel()
	 * @brief create Label + TextField panel from components
	 * @param label component
	 * @param text field component
	 * @param gap - gap between components
	 * @return panel contain Label + TextField
	 */
	public static JPanel createLabelTextFieldPanel (JLabel label, Component text, int gap) {
		JPanel p = new JPanel();
		Dimension gapSize = new Dimension(gap,0);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(label);
		p.add(Box.createRigidArea(gapSize));
		p.add(text);
		return p;
	}
	
	/**
	 * @methods timeDiffSec()
	 * @brief calculate difference between two times object in seconds
	 * @param first time
	 * @param second time
	 * @return time difference
	 */
	public static int timeDiffSec (Time t1, Time t2){
		int diff = 0;
		
		diff = (t1.getHour_() - t2.getHour_())*3600 + (t1.getMinute_() - t2.getMinute_())*60 + (t1.getSecond_() - t2.getSecond_());
		
		return diff;
	}
	
	/**
	 * @methods timeDiffMin()
	 * @brief calculate difference between two times object in minutes
	 * @param first time
	 * @param second time
	 * @return time difference
	 */
	public static int timeDiffMin (Time t1, Time t2){
		int diff = 0;
		
		diff = (t1.getHour_() - t2.getHour_())*3600 + (t1.getMinute_() - t2.getMinute_())*60;
		
		return diff;
	}
	
	/**
	 * @methods createTimeSpinner()
	 * @brief create time model spinner
	 * @param spinner object
	 * @return spinner object with time model
	 */
	public static JSpinner createTimeSpinner (JSpinner spin){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

	    SpinnerDateModel model = new SpinnerDateModel();
	    model.setValue(calendar.getTime());
	    spin.setModel(model);
	   
	    JSpinner.DateEditor editor = new JSpinner.DateEditor(spin, "HH:mm:ss");
	    DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
	    formatter.setAllowsInvalid(false);
	    formatter.setOverwriteMode(true);
	    spin.setEditor(editor);
	    
	    return spin;
	}
	
	/**
	 * @methods calculateSignalData()
	 * @brief conversion raw signal to real value in mV
	 * @param raw signal sample
	 * @return real signal value
	 */
	private static final long referance_value = 2420;
	private static final long scope_value = 0x7FFF;
	public static double calculateSignalData (double sample) {
		double csample = 0;
		
		if(sample > 0){
			if (sample == scope_value)
				csample = referance_value;
			else
				csample = (referance_value * sample) / scope_value;
		}
		else {
			if (sample == -scope_value)
				csample = -referance_value;
			else
				csample = (referance_value * sample) / scope_value;
		}
		
		return csample;
	}
	
	public static double toDouble2Precision (double s){
		int tmp;
		double ret;
		tmp = (int)(s*100);
		ret = ((double)(tmp))/100;
		return ret;
	}
}
