package uds.birdmanbros.test.musician_db;

import java.util.LinkedList;
import java.util.Set;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class Artist {
//	@JsonbTransient
	static private RedisDB redis = null;
	static private Neo4jDB neo4j = null;
	@JsonbProperty("name")
	private String artistName;
	private LinkedList<String> roles;
//	@JsonbTransient
//	private StringBuilder stringBuilder;
//	private JsonbConfig config;
//	private Jsonb jsonb;
	
	
	
	Document createDocument() {
		Document result = new Document("name", artistName);
		
		if(!roles.isEmpty()) {
			result.append("roles", roles);
		}
		
		return result;
		
	}
	

	static public RedisDB getRedisDB() {
		return redis;
	}
	static public void setRedisDB(RedisDB rd) {
		redis = rd;
	}
	static public Neo4jDB getNeo4jDB() {
		return neo4j;
	}
	static public void setNeo4jDB(Neo4jDB n) {
		neo4j = n;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public LinkedList<String> getRoles() {
		return roles;
	}
	public void setRoles(LinkedList<String> roles) {
		this.roles = roles;
	}

	public Artist() {
//		roles = null;
//		config = new JsonbConfig().withFormatting(true);
//		config.withNullValues(true);
//		jsonb = JsonbBuilder.create(config);
		roles = new LinkedList<String>();
//		stringBuilder = new StringBuilder("artist:");
	}

	public Artist(String artist, String bandName) {
		this();
		artistName = artist.trim();
		// System.out.format("DEBUG>> %s%n", artistName);
		
		StringBuilder stringBuilder = new StringBuilder("artist:");

//		stringBuilder.setLength("artist:".length());
		stringBuilder.append(bandName).append(":").append(artistName);
		Set<String> roles_sstr = redis.smembers(stringBuilder.toString());
//		 System.out.format("DEBUG2>> %s%n", roles_sstr.toString());

		for (String role : roles_sstr) {
//			if(roles == null) { roles = new LinkedList<String>();}
			roles.add(role.trim());
//			System.out.format("DEBUG3>> %s%n", role);
		}
//		roles.add("DJ Police");

	}

}
