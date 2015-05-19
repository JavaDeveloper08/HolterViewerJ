package adapters;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;

public class FileAdapter {
	
	protected File file;
	protected BufferedReader bufferedReader;
	protected BufferedWriter bufferedWriter;
	protected FileInputStream fileInputStream;
	protected FileOutputStream fileOutputStream;
	protected String cellSeparator;
	protected String lineSeparator;
	protected FileChannel fileInputChannel;
	
	/**
	 * Constructor.
	 * 
	 * @param filePath
	 *            Absolute file path.
	 */
	public FileAdapter(String filePath){
		file = new File(filePath);
		this.cellSeparator = null;
		this.lineSeparator = null;
	}

	/**
	 * Constructor dedicated for csv files.
	 * 
	 * @param filePath
	 *            Absolute file path
	 * @param cellSeparator
	 *            csv cell separator
	 * @param lineSeparator
	 *            csv row separator
	 */
	public FileAdapter(String filePath, String cellSeparator, String lineSeparator){
		file = new File(filePath);
		this.cellSeparator = cellSeparator;
		this.lineSeparator = lineSeparator;
	}
	
	public boolean exists(){
		return file.exists();
	}
	
	/**
	 * Creating new directory.
	 * @param path direcotry path
	 * @return
	 */
	public static boolean createDirecotry(String path){
		boolean result = false;
		try{
			result = (new File(path)).mkdirs();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Getting current file name
	 * @return file name
	 */
	public String getFileName(){
		if(file == null) return null;
		return file.getName();
	}
	
	/**
	 * Open file to standard read.
	 */
	public void openToRead(){
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Open file to binary read.
	 */
	public void openToBinaryRead(){
		try {
			fileInputStream = new FileInputStream(file);
			fileInputChannel = fileInputStream.getChannel();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Reads bytes to buffer.
	 * @param buff 
	 * @return
	 */
	public int readBytes(byte[] buff){
		int bytesNum = 0;
		ByteBuffer dsts = ByteBuffer.allocate(buff.length);
		byte[] tmp;
		try {
			bytesNum = fileInputChannel.read(dsts);
			tmp = dsts.array();
			for(int i = 0; i < buff.length; i++){
				buff[i] = tmp[i];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytesNum;
	}
	/**
	 * Read one line from file.
	 * @return String or null if end of file has been reached
	 */
	public String readLine(){
		String line = null;
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
	protected void createFileAndDirs() throws IOException{
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}
	
	/**
	 * Open or create file to standard write.
	 */
	public void openOrCreateToWrite() {
		try {
			createFileAndDirs();
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Open or create file to binary write.
	 */
	public void openToBinaryWrite(){
		try {
			createFileAndDirs();
			fileOutputStream = new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writing string to opened file.
	 * 
	 * @param str
	 *            - input String
	 */
	public void write(String str) {
		if (file.canWrite()) {
			try {
				bufferedWriter.write(str);
				bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Write bytes to file.
	 * @param b byte array
	 */
	public void writeBytes(byte[] b){
		try {
			fileOutputStream.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Write string line to file.
	 * @param line
	 */
	public void writeLine(String line){
		if(lineSeparator == null){
			line += System.getProperty("line.separator");
			write(line);
		} else {
			line += lineSeparator;
			write(line);
		}
	}
	/**
	 * Write cells to csv file.
	 * @param cells
	 */
	public void writeCSVLine(String[] cells){
		StringBuilder line = new StringBuilder();
		for(int i = 0; i < cells.length; i++){
			line.append(cells[i]);
			
			if(i == cells.length - 1)
				break;
			
			line.append(cellSeparator);
		}
		line.append(lineSeparator);
		write(line.toString());
	}
	/**
	 * Close this adapter.
	 */
	public void close(){
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (fileInputStream != null){
				fileInputStream.close();
			}
			if (fileOutputStream != null){
				fileOutputStream.close();
			}
			if(fileInputChannel != null){
				fileInputChannel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
