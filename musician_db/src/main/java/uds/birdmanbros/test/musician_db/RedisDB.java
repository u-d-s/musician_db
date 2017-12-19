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
	
	public void set(String key, String value) {
		redisSyncCommands.set(key, value);
	}
	
	public void expire(String key, long expiry) {
		redisSyncCommands.expire(key, expiry);
	}
	
	public String get(String key) {
		return redisSyncCommands.get(key);
	}
	
	public void setex(String key, long expiry, String value) {
		redisSyncCommands.setex(key,expiry,value);
	}
	
	
	@Override
	public void close() {
		redisConnection.close();
		redisClient.shutdown();
		System.out.format("RedisDB has closed; %s %d %n", uri, DBNumber);
	}
	
	
	public RedisDB(String u, int n) {
		uri = u;
		DBNumber = n;
		redisClient = RedisClient.create(uri + DBNumber);
		redisConnection = redisClient.connect();
		redisSyncCommands = redisConnection.sync();
	}
	
	public RedisDB() {
		this("redis://localhost:6379/", 15);
	}
	


}
