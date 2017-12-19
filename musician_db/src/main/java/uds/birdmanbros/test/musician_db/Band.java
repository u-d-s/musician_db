package uds.birdmanbros.test.musician_db;

import java.util.LinkedList;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;

import org.bson.Document;

public class Band {
//	@JsonbTransient
	static private RedisDB redis = null;
//	@JsonbTransient
	static private MongoDB mongo = null;
	static private Neo4jDB neo4j = null;
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
//			artists.add(new Artist(artist_str, bandName));
			addArtist(new Artist(artist_str, bandName));
		}
	}

	public Document createDocument() {
		LinkedList<Document> artist_docs = new LinkedList<>();
		for(Artist artist: artists) {
			artist_docs.add(artist.createDocument());
		}
		
		return new Document("_id",bandName)
							.append("artists", artist_docs);
	}
	
	public void addArtist(Artist artist) {
		artists.add(artist);
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
	public static Neo4jDB getNeo4jDB() {
		return neo4j;
	}
	public static void setNeo4jDB(Neo4jDB neo4j) {
		Band.neo4j = neo4j;
	}
	

	
	
	public Band() {
		artists = new LinkedList<Artist>();
	}

}
	
	
