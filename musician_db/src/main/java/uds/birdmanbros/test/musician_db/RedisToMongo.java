package uds.birdmanbros.test.musician_db;

import java.util.*;

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
	
	public void run() {
		System.out.format("RedisToMongo>> run%n");
		
    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
    	StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
    	RedisCommands<String, String> redisSyncCommands = redisConnection.sync();
    	
//    	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//    	MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
//    	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("myTestCollection");
		
		
    	
    	String keyOfArtists_rex = "band:U*";
    	
		Band.setRedisCommands(redisSyncCommands);
		Artist.setRedisCommands(redisSyncCommands);
		Band band = new Band();
//		band.setMongoCollection(mongoCollection);
		
		List<String> keyOfArtists = redisSyncCommands.keys(keyOfArtists_rex);
		
		int i=0;
		for(String koa: keyOfArtists) {
			band.setKeyOfArtists(koa);
			band.writeIntoMongoDB();
//			System.out.format("%d DEBUG>> %s%n", i++,koa);
		}
		
		
    	redisConnection.close();
    	redisClient.shutdown();
    	
//    	mongoClient.close();
		
	}
	
	

	
	static private class Band{
		static private RedisCommands<String, String> redisCommands;
		static private MongoCollection<Document> mongoCollection;
		private String bandName;
		private LinkedList<Artist> artists;
		
//		public long getKeyOfBands(String kob_rex) {
//			// set keyOfBands;
//			
//			keyOfBands_rex = kob_rex;
//			keyOfBands = redisCommands.keys(keyOfBands_rex);
//			
////			Jsonb jsonb = JsonbBuilder.create();
////			System.out.format("DEBUG>> %s%n",  jsonb.toJson(keyOfBands));
//			
//			return keyOfBands.size();
//		}
		
		public void setKeyOfArtists(String keyOfArtists) {
			bandName = keyOfArtists.trim().substring(5);
			Set<String> artists_sstr = redisCommands.smembers(keyOfArtists);
			
			for(String artist_str: artists_sstr) {
				artists.add(new Artist(artist_str, bandName));
			}
		}
		
		
		public void writeIntoMongoDB() {
			;
		}

		static public RedisCommands<String, String> getRedisCommands() {
			return redisCommands;
		}

		static public void setRedisCommands(RedisCommands<String, String> rc) {
			redisCommands = rc;
		}

		static public MongoCollection<Document> getMongoCollection() {
			return mongoCollection;
		}

		static public void setMongoCollection(MongoCollection<Document> mc) {
			mongoCollection = mc;
		}
		
		
		public Band() {
			artists = new LinkedList<Artist>();
		}
		
		
	}
	
	
	static private class Artist{
		static private RedisCommands<String, String> redisCommands;
		static private MongoCollection<Document> mongoCollection;
		private String artistName;
		private LinkedList<String> roles;
		private StringBuilder stringBuilder;
		

		
		
		static public RedisCommands<String, String> getRedisCommands() {
			return redisCommands;
		}
		static public void setRedisCommands(RedisCommands<String, String> rc) {
			redisCommands = rc;
		}
		static public MongoCollection<Document> getMongoCollection() {
			return mongoCollection;
		}
		static public void setMongoCollection(MongoCollection<Document> mc) {
			mongoCollection = mc;
		}
		
		public Artist() {
			roles = new LinkedList<String>();
			stringBuilder = new StringBuilder("artist:");
		}
		public Artist(String artist, String bandName) {
			this();
			artistName = artist.trim();
//			System.out.format("DEBUG>> %s%n", artistName);
			
			stringBuilder.setLength("artist:".length());
			stringBuilder.append(bandName).append(":").append(artistName);
			Set<String> roles_sstr = redisCommands.smembers(stringBuilder.toString());
//			System.out.format("DEBUG>> %s%n", stringBuilder.toString());
			
			for(String role: roles_sstr) {
				roles.add(role.trim());
//				System.out.format("DEBUG>> %s%n", role);
			}
			
		}
	}
}