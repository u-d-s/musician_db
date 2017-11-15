package uds.birdmanbros.test.musician_db;

import java.io.*;
import java.nio.file.*;

public class TsvFile implements Closeable {
	private Path relativePath;
	private Path currentPath;
	
	public void setRelativePath(String rp) {
		relativePath = Paths.get(rp);
	}
	
	public String getCurrentPath(){
		return currentPath.toString();
	}
	
	public String getAbsolutePath() {
		return currentPath.resolve(relativePath).normalize().toString(); 
	}
	
	
	public TsvFile(){
		currentPath = Paths.get(".").toAbsolutePath();
	}
	
	public TsvFile(String p){
		this();
		setRelativePath(p);
	}

	@Override
	public void close() throws IOException {
		// TODO 自動生成されたメソッド・スタブ

	}

}
