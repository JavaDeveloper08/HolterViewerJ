package mvc;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	static JPanel createLabelTextFieldPanel (JLabel label, JTextField text, int gap) {
		JPanel p = new JPanel();
		Dimension gapSize = new Dimension(gap,0);
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.add(label);
		p.add(Box.createRigidArea(gapSize));
		p.add(text);
		return p;
	}
}
