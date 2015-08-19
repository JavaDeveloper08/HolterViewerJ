package adapters;

import java.io.*;

import mvc.AppException;

/**
 * @class FileAdapter
 * @brief adapter to file operations provide creating, reading and writing functions
 */
public class FileAdapter {
	
	private File file;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String cellSeparator;
	private String lineSeparator;
	
	public FileAdapter(){
		file = null;
		cellSeparator = null;
		lineSeparator = null;
	}
	
	public FileAdapter(String filePath, String cs, String ls){
		file = new File(filePath);
		cellSeparator = cs;
		lineSeparator = ls;
	}
	
	 /**
     * @methods createDirectory()
     * @brief create directory with given path
     * @param path - path to directory space
     * @return true if the directory was created, false otherwise
     */
	public static boolean createDirectory(String path){
		boolean result = false;
		try{
			result = (new File(path)).mkdirs();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	 /**
     * @methods createFileAndDirs()
     * @brief create directory and file in current space
     * @throws IOException
     */
	protected void createFileAndDirs() throws IOException{
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
	}
	
	 /**
     * @methods openToRead()
     * @brief open file to read data
     */
	public void openToRead() throws AppException {
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			throw new AppException("B³¹d odczytu pliku");
		}
	}
	
	 /**
     * @methods readLine()
     * @brief read line from file
     * @return String data line
     */
	public String readLine() throws AppException{
		String line = null;
		try {
			line = bufferedReader.readLine();
		} catch (IOException e) {
			throw new AppException("B³¹d odczytu pliku");
		}
		return line;
	}
	
	
	 /**
     * @methods openOrCreateToWrite()
     * @brief if file not exists create and open to write
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
     * @methods write()
     * @brief write single String data to file
     * @param str - String data
     */
	protected void write(String str) {
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
     * @methods writeLine()
     * @brief write String line completed by line separator
     * @param str - String data
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
     * @methods writeCSVLine()
     * @brief write String data line formated to CVS, add cell separators and line separator 
     * @param cells - String array of data 
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
     * @methods close()
     * @brief close file
     */
	public void close(){
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
