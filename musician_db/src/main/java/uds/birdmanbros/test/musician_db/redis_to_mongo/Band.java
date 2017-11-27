package uds.birdmanbros.test.musician_db.redis_to_mongo;

import java.util.LinkedList;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;

import uds.birdmanbros.test.musician_db.MongoDB;
import uds.birdmanbros.test.musician_db.RedisDB;

public class Band {
//	@JsonbTransient
	static private RedisDB redis = null;
//	@JsonbTransient
	static private MongoDB mongo = null;
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
		Set<String> artists_sstr = redis.smembers(keyOfArtists);
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

	static public RedisDB getRedis() {
		return redis;
	}

	static public void setRedisDB(RedisDB rd) {
		redis = rd;
	}
	
	public static MongoDB getMongoDB() {
		return mongo;
	}

	public static void setMongoDB(MongoDB mongo) {
		Band.mongo = mongo;
	}

	public Band() {
		artists = new LinkedList<Artist>();
	}

}
	
	
