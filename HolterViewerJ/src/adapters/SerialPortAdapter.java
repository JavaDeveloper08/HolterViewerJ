package adapters;

import jssc.*;

/**
 * @class SerialPortAdapter
 * @brief basic connection application with serial port
 */
public class SerialPortAdapter{

    private SerialPort serialPort;
    private boolean listening;
  
    public SerialPortAdapter() {      
        serialPort = null;
        listening= false;
    }
    
    /**
     * @methods getPortsNames()
     * @brief getting names of available ports
     * @return names list
     */
    public String[] getPortsNames() {   
    	return SerialPortList.getPortNames();
    }
    
    /**
     * @methods connect()
     * @brief connect to specific port with given parameters
     * @param portName - serial port name
     * @param baud - connection baud rate
     * @param dataBits - number of data bits
     * @param stopBits - number of stop bits
     * @param parity - type of parity
     * @throws SerialPortException
     */
    public void connect(String portName, int baud, int dataBits, int stopBits, int parity) throws SerialPortException {
    	serialPort = new SerialPort(portName);
        serialPort.openPort();
        serialPort.setParams(baud, dataBits, stopBits, parity);
        serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
    }
    
    /**
     * @methods disconnect()
     * @brief closing actual port connection
     * @throws SerialPortException
     */
    public void disconnect() throws SerialPortException{
    	if(serialPort != null && serialPort.isOpened()){
    		serialPort.closePort();
    	}
    }
    
    /**
     * @methods isConnected()
     * @brief getting connection status
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
    	if(serialPort == null)
    		return false;
    	return serialPort.isOpened();
    }
    
    /**
     * @methods startPortListening()
     * @brief start listening  for data
     * @param listener - serial port event listener
     * @throws SerialPortException
     */
    public void startPortListening(SerialPortEventListener listener) throws SerialPortException{
    	if(!listening && serialPort != null) {
    		serialPort.readBytes();
    		serialPort.addEventListener(listener);
    		listening = true;
    	}
    }
    
    /**
     * @methods stopPoartListening()
     * @brief stop listening for data
     * @throws SerialPortException
     */
    public void stopPortListening() throws SerialPortException {
    	if(listening && serialPort != null) {
    		serialPort.removeEventListener();
    		listening = false;
    	}
    }
    
    /**
     * @methods writeBytesToPort()
     * @brief sending bytes array to serial port
     * @param b - bytes array
     * @throws SerialPortException
     */
    public void writeBytesToPort(byte[] b) throws SerialPortException {
    	if(serialPort != null)
    		serialPort.writeBytes(b);
    }
    
    /**
     * @methods readBytesFromPort()
     * @brief read bytes array form serial port
     * @return bytes array
     */
    public byte[] readBytesFromPort(){
    	byte[] b = null;
		try {
			b = serialPort.readBytes();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	return b;	
    }
}
