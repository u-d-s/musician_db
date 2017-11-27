package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.util.List;
import java.util.Set;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class RedisDB implements Closeable{
	private RedisClient redisClient;
	private StatefulRedisConnection<String, String> redisConnection;
	private RedisCommands<String, String> redisSyncCommands;
	private String uri;
	private int DBNumber;
	
	
	public Set<String> smembers(String key){
		return redisSyncCommands.smembers(key);
	}
	
	public List<String> keys(String key_rex){
		return redisSyncCommands.keys(key_rex);
	}
	
	
	
	@Override
	public void close() {
		redisConnection.close();
		redisClient.shutdown();
		System.out.format("RedisDB has closed%n");
	}
	
	
	public RedisDB() {
		uri = "redis://localhost:6379/";
		DBNumber = 15;
		redisClient = RedisClient.create(uri + DBNumber);
		redisConnection = redisClient.connect();
		redisSyncCommands = redisConnection.sync();
	}

}
