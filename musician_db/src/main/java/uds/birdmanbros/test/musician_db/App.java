package uds.birdmanbros.test.musician_db;

import java.io.*;
import java.util.Arrays;

/*
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
*/


/*
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
*/

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "alo!" );
        
        try(TsvFile tsvFile = new TsvFile("src\\main\\resources\\group_membership_short.tsv")){
            System.out.format("current>> %s%ntsv>> %s%n", tsvFile.getCurrentPath(), tsvFile.getAbsolutePath());
            
            tsvFile.setCharset("UTF-8");
            
            tsvFile.readHeader();
            
            System.out.format("header>> %s%n", String.join(",", tsvFile.getHeader()));           
            System.out.format("maxColumn>> %d%n", tsvFile.getMaxColumn());	
            
            try {
            	String[] data;
            	App.LineDataSet lineDataSet = new App.LineDataSet();
            	while(true) {
//            		System.out.format("tcv> %s%n", String.join(",", tsvFile.readLine()));
            		data = tsvFile.readLine();
            		lineDataSet.setData(data[3],data[2],data[4]);
            		System.out.format("1>> %s%n", lineDataSet.toString());
            		System.out.format("2>> %s%n", lineDataSet.hasRoles());
//            		artist = data[2];
//            		band = data[3];
//            		roles = data[4];
 
            	}
            }catch(EOFException eof) {
            	System.out.format("Ive finished reading the tsv file%n");
            	System.out.format("  %s%n",  eof.toString());
            }catch(IOException x) {
            	throw x;
            }
            
    
        }catch(IOException x) {
			System.err.format("IOException: %s%n", x);
        }

        /*
		Path current_path = Paths.get(".");
		System.out.format(">>> %s%n", current_path.toAbsolutePath());
		
		Path tsv_file_path = Paths.get("src\\main\\resources\\group_membership_short.tsv");
		System.out.format(";->  %s%n", tsv_file_path.toAbsolutePath());
		
		try (BufferedReader reader = Files.newBufferedReader(tsv_file_path, Charset.forName("UTF-8"))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	System.out.format(";->  %s%n",line);
		    }
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
         */
        /*
        
    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
    	StatefulRedisConnection<String, String> connection = redisClient.connect();
    	RedisCommands<String, String> syncCommands = connection.sync();

//    	syncCommands.set("lettuce", "Hello, Redis!");
//    	String value;
//    	value = syncCommands.get("lettuce");
//    	System.out.format("value= %s%n", value);
    	syncCommands.sadd("slettuce", "alo");
    	Set<String> svalue;
    	svalue = syncCommands.smembers("slettuce");
    	System.out.format("value= %s%n", svalue.toString());

    	connection.close();
    	redisClient.shutdown();
    	*/
    	
    }
    
    static private class LineDataSet {
    	private String band;
    	private Artist artist;
    	
    	public void setData(String b, String a, String r) {
    		band = b;
    		artist.setData(a, r);
    	}
    	public String getBand() { return band; }
    	
    	public String toString() {
    		return "band: "+band+"| "+artist.toString();
    	}
    	
    	public boolean isValid() {
    		return band.equals("") || (artist.getArtist().equals(""));
    	}
    	
    	public boolean hasRoles() {
    		return artist.hasRoles();
    	}
    	
    	public String getKeyOfArtist() {
    		return "band:" +band;
    	}
    	
    	public String getKeyOfRoles() {
    		return "artist:"+band+":"+artist.getArtist();
    	}
    	
    	
    	public LineDataSet() {
    		artist = new Artist();
    	}
    }
    
    static private class Artist {
    	private String artist;
    	private String[] roles;
    	
    	
    	public String getArtist() { return artist; }
    	
    	public boolean hasRoles() {
    		return !(Arrays.equals(roles, new String[]{}));
    	}
    	
    	public String toString() {
    		return "artist: "+artist+"| roles: "+String.join("/", roles);
    	}
    	
    	public void setData(String a,String r) {
    		artist = a;
    		roles = r.equals("") ? new String[] {} : r.split(",",0);
    		for(int i=0;i<roles.length;i++) { roles[i] = roles[i].trim(); }
    	}
    	
    }
}
