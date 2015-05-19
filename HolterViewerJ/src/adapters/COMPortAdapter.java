package adapters;

import jssc.*;

public class COMPortAdapter{

    private SerialPort serialPort;
    private boolean listening;
  
    public COMPortAdapter() {      
        serialPort = null;
        listening= false;
    }
    
    /**
     * Getting names of available ports.
     * @return names list
     */
    public String[] getPortsNames() {   
    	return SerialPortList.getPortNames();
    }
    
    /**
     * Connect to specific port with given params.
     * @param portName - COMM port name
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
        int mask = SerialPort.MASK_RXCHAR;
        serialPort.setEventsMask(mask);
    }
    
    /**
     * Closing actual connection.
     * @throws SerialPortException
     */
    public void disconnect() throws SerialPortException{
    	if(serialPort != null && serialPort.isOpened()){
    		serialPort.closePort();
    	}
    }
    
    /**
     * Getting connection status.
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
    	if(this.serialPort == null)
    		return false;
    	return this.serialPort.isOpened();
    }
    
    /**
     * Start listening for data.
     * @throws SerialPortException
     */
    public void startPortListening(SerialPortEventListener listener) throws SerialPortException{
    	if(!this.listening && serialPort != null) {
    		serialPort.readBytes();
    		serialPort.addEventListener(listener);
    		this.listening = true;
    	}
    }
    
    /**
     * Stop listening for data.
     * @throws SerialPortException
     */
    public void stopPortListening() throws SerialPortException {
    	if(this.listening && serialPort != null) {
    		serialPort.removeEventListener();
    		this.listening = false;
    	}
    }
    
    public void writeByteToPort(byte b) throws SerialPortException{
    	if(serialPort != null)
    		serialPort.writeByte(b);
    }
    
    /**
     * Sending byte array to port.
     * @param b - data
     * @throws SerialPortException
     */
    public void writeBytesToPort(byte[] b) throws SerialPortException {
    	if(this.serialPort != null)
    		this.serialPort.writeBytes(b);
    }
    
    /**
     * Sending String to port.
     * @param s - data
     * @throws SerialPortException
     */
    public void writeToPort(String s) throws SerialPortException {
    	this.serialPort.writeString(s);
    } 
}
