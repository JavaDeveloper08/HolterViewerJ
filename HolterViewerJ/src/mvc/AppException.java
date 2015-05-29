package mvc;

import javax.swing.JOptionPane;

/**
 * @class AppException
 * @brief class representing application exceptions
 * @extends Exception class
 */

public class AppException extends Exception{
	private static final long serialVersionUID = 1L;
	
	/** default constructors */
	public AppException(){
		super();	
	}
	
	/** parameterized constructors */
	public AppException(String s){
		super(s);
	}
	
	/**
	 * @fn show_exception()
	 * @brief show exception message
	 */
	public void show_exception() {
		JOptionPane.showMessageDialog(null, this.getMessage(), "Application error", JOptionPane.ERROR_MESSAGE);
	}
}
