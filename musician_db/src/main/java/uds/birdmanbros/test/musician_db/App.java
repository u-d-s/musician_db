package uds.birdmanbros.test.musician_db;

import java.io.IOException;

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
            
            System.out.format("str>> %s%n", tsvFile.getLineBuffer_str());
            System.out.format("arr[0]>> %s%n", tsvFile.getLineBuffer_arr()[0]);           
            System.out.format("arr[1]>> %s%n", tsvFile.getLineBuffer_arr()[1]);	
            System.out.format("maxColumn>> %d%n", tsvFile.getMaxColumn());	
            
    
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
}
