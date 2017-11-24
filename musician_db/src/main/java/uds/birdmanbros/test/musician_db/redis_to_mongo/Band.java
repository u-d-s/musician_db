package uds.birdmanbros.test.musician_db.redis_to_mongo;

import java.util.LinkedList;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

import org.bson.Document;

import com.lambdaworks.redis.api.sync.RedisCommands;
import com.mongodb.client.MongoCollection;

public class Band {
	@JsonbTransient
	static private RedisCommands<String, String> redisCommands = null;
	@JsonbTransient
	static private MongoCollection<Document> mongoCollection = null;
	@JsonbProperty("name")
	private String bandName;
	private LinkedList<Artist> artists;

	// public long getKeyOfBands(String kob_rex) {
	// // set keyOfBands;
	//
	// keyOfBands_rex = kob_rex;
	// keyOfBands = redisCommands.keys(keyOfBands_rex);
	//
	//// Jsonb jsonb = JsonbBuilder.create();
	//// System.out.format("DEBUG>> %s%n", jsonb.toJson(keyOfBands));
	//
	// return keyOfBands.size();
	// }

	public void updateBand(String keyOfArtists) {
		bandName = keyOfArtists.trim().substring(5);
		
		artists.clear();
		Set<String> artists_sstr = redisCommands.smembers(keyOfArtists);
		for (String artist_str : artists_sstr) {
			artists.add(new Artist(artist_str, bandName));
		}
	}

	public void writeIntoMongoDB() {
		;
	}

	public String getBandName() {
		return bandName;
	}

	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	public LinkedList<Artist> getArtists() {
		return artists;
	}

	public void setArtists(LinkedList<Artist> artists) {
		this.artists = artists;
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
	
	
