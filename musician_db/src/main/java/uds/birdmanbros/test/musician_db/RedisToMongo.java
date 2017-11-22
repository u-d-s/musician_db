package uds.birdmanbros.test.musician_db;

import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.bson.Document;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class RedisToMongo {
	private String keyOfBands_rex; 
	
	public void run() {
		System.out.format("RedisToMongo>> run%n");
		
    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
    	StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
    	RedisCommands<String, String> redisSyncCommands = redisConnection.sync();
    	
//    	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//    	MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
//    	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("myTestCollection");
		
		
		//（方針）
    	// Band は「一つの」バンドに関する処理をおこなう
    	// 複数のBandの繰りまわりは、呼び出しもとメソッドで行う
    	
		
		Band band = new Band();
		band.setRedisCommands(redisSyncCommands);
//		band.setMongoCollection(mongoCollection);
		
		long len_kob = band.getKeyOfBands(keyOfBands_rex);
		
		for(int i=0; i<len_kob;i++) {
			band.setArtist();
			band.writeIntoMongoDB();
		}
		
		
    	redisConnection.close();
    	redisClient.shutdown();
    	
//    	mongoClient.close();
		
	}
	
	
	
	public RedisToMongo() {
		keyOfBands_rex = "band:U*";
	}
	
	
	static private class Band{
		private List<String> keyOfBands;
		private String keyOfBands_rex;
		private RedisCommands<String, String> redisCommands;
		private MongoCollection<Document> mongoCollection;
		
		public long getKeyOfBands(String kob_rex) {
			// set keyOfBands;
			
			keyOfBands_rex = kob_rex;
			keyOfBands = redisCommands.keys(keyOfBands_rex);
			
//			Jsonb jsonb = JsonbBuilder.create();
//			System.out.format("DEBUG>> %s%n",  jsonb.toJson(keyOfBands));
			
			return keyOfBands.size();
		}
		
		public void setArtist() {
			;
		}
		
		public void writeIntoMongoDB() {
			;
		}

		public RedisCommands<String, String> getRedisCommands() {
			return redisCommands;
		}

		public void setRedisCommands(RedisCommands<String, String> redisCommands) {
			this.redisCommands = redisCommands;
		}

		public MongoCollection<Document> getMongoCollection() {
			return mongoCollection;
		}

		public void setMongoCollection(MongoCollection<Document> mongoCollection) {
			this.mongoCollection = mongoCollection;
		}
		
		
		
	}
}