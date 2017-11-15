package uds.birdmanbros.test.musician_db;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;

public class TsvFile implements Closeable{
	private Path relativePath;
	private Path currentPath;
	private Charset charset;
	private String delimiter;
	private long maxRow;
	private int maxColumn;
	private long currentRow;
	private String[] lineBuffer_arr;
	private String lineBuffer_str;
	private BufferedReader reader;	
	
	public void setRelativePath(String rp) {
		relativePath = Paths.get(rp);
	}
	
	public void setCharset(String cs) {
		charset = Charset.forName(cs);
	}
	
	public int getMaxColumn() {
		return maxColumn;
	}
	
	public String getLineBuffer_str() {
		return lineBuffer_str;
	}
	
	public String[] getLineBuffer_arr() {
		return lineBuffer_arr;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	

	
	public void readHeader() throws IOException {
		reader = Files.newBufferedReader(relativePath, charset);
		lineBuffer_str = reader.readLine();
		lineBuffer_arr = lineBuffer_str.split(delimiter);
		maxColumn = lineBuffer_arr.length;
	}
	
	public String getCurrentPath(){
		return currentPath.toString();
	}
	
	public String getAbsolutePath() {
		return currentPath.resolve(relativePath).normalize().toString(); 
	}
	
	
	public TsvFile(){
		currentPath = Paths.get(".").toAbsolutePath();
		charset = Charset.forName("UTF-8");
		delimiter = "\t";
		maxRow = -1;
		maxColumn = -1;
		currentRow = -1;
	}
	
	public TsvFile(String p){
		this();
		setRelativePath(p);
	}

	@Override
	public void close() throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		reader.close();
		System.out.format("TsvFile has closed%n");

	}

}
