package uds.birdmanbros.test.musician_db;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;

public class TsvFile implements Closeable{
	private Path relativePath;
	private Path currentPath;
	private Charset charset;
	private String delimiter;
	private long dataLines;
	private int maxColumn;
	private long processedLines;
	private String lineBuffer;
	private String[] header;
	private BufferedReader reader;	
	private int printProcessedLinesEvery;
	
	public void setRelativePath(String rp) {
		relativePath = Paths.get(rp);
	}
	
	public void setCharset(String cs) {
		charset = Charset.forName(cs);
	}
	
	public int getMaxColumn() {
		return maxColumn;
	}
	
	public String getLineBuffer() {
		return lineBuffer;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	
	public String[] getHeader() {
		return header;
	}
	
	public int getPrintProcessedLinesEvery() {
		return printProcessedLinesEvery;
	}

	public void setPrintProcessedLinesEvery(int printProcessedLinesEvery) {
		this.printProcessedLinesEvery = printProcessedLinesEvery;
	}
	
	
	

	public void readHeader() throws IOException {
		reader = Files.newBufferedReader(relativePath, charset);
		lineBuffer = reader.readLine();
		header = lineBuffer.split(delimiter);
		for(int i=0;i<header.length;i++){ header[i] = header[i].trim(); } 
	
//			System.out.format("DEBUG> %s%n", header[i]);
//			System.out.format("DEBUG> %s%n", header[i].trim());
//			
//			header[i] = header[i].trim(); }
//		String str = new String(header[0].getBytes("SJIS"), "SJIS");
//		byte[] b = str.getBytes();
//		byte[] bt = str.trim().getBytes();
//		System.out.format("DEBUG1> ");
//		for(int i=0;i<b.length;i++) {
//			System.out.format("%d ", b[i]);	
//		}
//		System.out.format("%n");
//		System.out.format("DEBUG2> ");
//		for(int i=0;i<bt.length;i++) {
//			System.out.format("%d ", bt[i]);	
//		}
//		System.out.format("%n");
//		System.out.format("DEBUG1 %s %s%n", str, Arrays.toString(b));
//		System.out.format("DEBUG2 %s %s%n",str,  Arrays.toString(bt));
	
		
		
		maxColumn = header.length;
		processedLines = 1;
	}
	
	public String[] readLine() throws IOException {
		lineBuffer = reader.readLine();	
		
		if(lineBuffer == null){
			dataLines = processedLines-1;
			throw new EOFException("total data lines processed: " + dataLines);
		}
		
//		System.out.format("DEBUG1> %s%n", lineBuffer_str);
//		System.out.format("DEBUG2> %s%n", String.join(",",lineBuffer_arr));
	
		
		String[] result = lineBuffer.split(delimiter,-1);
		if(result.length != maxColumn) {
			throw new IOException("too much/little columns ("+result.length+" columns) at data line "+(processedLines+1)+".");
		}
		for(int i=0;i<result.length;i++) { result[i] = result[i].trim(); }
		
		processedLines++;
		printProcessedLines();
		
		return result;
	}
	
	void printProcessedLines() {
		if(processedLines % printProcessedLinesEvery == 0) {
			System.out.format("processedLine>> %d%n", processedLines);
		}
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
		dataLines = -1;
		maxColumn = -1;
		processedLines = -1;
		printProcessedLinesEvery = 10;
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
