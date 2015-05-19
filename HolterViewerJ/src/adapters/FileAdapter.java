package adapters;

import java.io.*;

public class FileAdapter {
	
	protected File file;
	protected BufferedReader bufferedReader;
	protected BufferedWriter bufferedWriter;
	protected String cellSeparator;
	protected String lineSeparator;
	
	public FileAdapter(){
		file = null;
		this.cellSeparator = null;
		this.lineSeparator = null;
	}
	
	public FileAdapter(String filePath, String cellSeparator, String lineSeparator){
		file = new File(filePath);
		this.cellSeparator = cellSeparator;
		this.lineSeparator = lineSeparator;
	}
	
	public boolean exists(){
		return file.exists();
	}
	
	public static boolean createDirectory(String path){
		boolean result = false;
		try{
			result = (new File(path)).mkdirs();
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String getFileName(){
		if(file == null) return null;
		return file.getName();
	}
	
	public void openToRead(){
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	public void openOrCreateToWrite() {
		try {
			createFileAndDirs();
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	public void writeLine(String line){
		if(lineSeparator == null){
			line += System.getProperty("line.separator");
			write(line);
		} else {
			line += lineSeparator;
			write(line);
		}
	}

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
