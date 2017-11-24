package uds.birdmanbros.test.musician_db.redis_to_mongo;

import java.util.LinkedList;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

import org.bson.Document;

import com.lambdaworks.redis.api.sync.RedisCommands;
import com.mongodb.client.MongoCollection;

public class Artist {
	@JsonbTransient
	static private RedisCommands<String, String> redisCommands;
	@JsonbTransient
	static private MongoCollection<Document> mongoCollection;
	@JsonbProperty("name")
	private String artistName;
	private LinkedList<String> roles;
	
//	@JsonbTransient
//	private StringBuilder stringBuilder;

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
		roles = null;
//		roles = new LinkedList<String>();
//		stringBuilder = new StringBuilder("artist:");
	}

	public Artist(String artist, String bandName) {
		this();
		artistName = artist.trim();
		// System.out.format("DEBUG>> %s%n", artistName);
		
		StringBuilder stringBuilder = new StringBuilder("artist:");

//		stringBuilder.setLength("artist:".length());
		stringBuilder.append(bandName).append(":").append(artistName);
		Set<String> roles_sstr = redisCommands.smembers(stringBuilder.toString());
//		 System.out.format("DEBUG2>> %s%n", roles_sstr.toString());

		for (String role : roles_sstr) {
			if(roles == null) { roles = new LinkedList<String>();}
			roles.add(role.trim());
//			System.out.format("DEBUG3>> %s%n", role);
		}
//		roles.add("DJ Police");

	}

}
