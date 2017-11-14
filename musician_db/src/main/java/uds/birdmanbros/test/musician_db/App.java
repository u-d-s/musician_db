package uds.birdmanbros.test.musician_db;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "alo!" );
 
    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
    	StatefulRedisConnection<String, String> connection = redisClient.connect();
    	RedisCommands<String, String> syncCommands = connection.sync();

    	syncCommands.set("lettuce", "Hello, Redis!");
    	String value;
    	value = syncCommands.get("lettuce");
    	System.out.format("value= %s%n", value);

    	connection.close();
   	redisClient.shutdown();
    }
}
