package mvc;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

import data.*;

/**
 * @class Utils
 * @brief class contains static utils methods for application
 */

public class Utils {
	
	/**
	 * @fn isText()
	 * @brief check if string line contain text
	 * @param string line
	 * @return true if line contain text
	 */
	static boolean isText (String line){
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
	 * @fn isNumber()
	 * @brief check if string line contain number
	 * @param string line
	 * @return true if line contain number
	 */
	static boolean isNumber (String line){
		if(line.isEmpty())
			return false;
		for(int i=0; i<line.length(); i++){
			if(!(Character.isDigit(line.charAt(i))))
					return false;
		}
		return true;
	}
	
	static JPanel createLabelTextFieldPanel (JLabel label, Component text, int gap) {
		JPanel p = new JPanel();
		Dimension gapSize = new Dimension(gap,0);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(label);
		p.add(Box.createRigidArea(gapSize));
		p.add(text);
		return p;
	}
	
	static int timeDiff (Time t1, Time t2){
		int diff = 0;
		
		diff = (t1.getHour_() - t2.getHour_())*3600 + (t1.getMinute_() - t2.getMinute_())*60 + (t1.getSecond_() - t2.getSecond_());
		
		return diff;
	}
	
	static int timeDiffWithoutSec (Time t1, Time t2){
		int diff = 0;
		
		diff = (t1.getHour_() - t2.getHour_())*3600 + (t1.getMinute_() - t2.getMinute_())*60;
		
		return diff;
	}
	
	static JSpinner crateTimeSpinner (JSpinner spin){
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
}
