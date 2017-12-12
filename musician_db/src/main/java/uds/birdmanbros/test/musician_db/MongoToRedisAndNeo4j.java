package uds.birdmanbros.test.musician_db;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MongoToRedisAndNeo4j implements Watcher {
	private MongoDB mongo;
	private RedisDB redis;
	private Neo4jDB neo4j;
	private long expiry;

	public void run() {
		System.out.format(">> MogoToRedisAndNeo4j%n");

		expiry = 60;

		mongo = new MongoDB();
		redis = new RedisDB("redis://localhost:6379/", 0);
		neo4j = new Neo4jDB();
		Band.setMongoDB(mongo);
		Band.setRedisDB(redis);
		Band.setNeo4jDB(neo4j);
		Artist.setRedisDB(redis);
		Artist.setNeo4jDB(neo4j);
		try {
			mongo.watchedBy(this);
			mongo.change();
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			mongo.close();
			redis.close();
			neo4j.close();
		}

	}

	public void change(Object obj) {
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

		PingBand band = (PingBand) obj;

		if ((band.getBandName() == null) || (band.getBandName().isEmpty())) {
			System.out.format("Band Name is null or empty >> %s%n", band.getBandName());
		} else {
			if (band.getArtists().size() == 0) {
				System.out.format("No artist.%n");
			} else {

				try {
					feedBandToRedis(band);
					feedBandToNeo4j(band);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				// }
				// }
			}
		}
	}

	private void feedBandToRedis(Band band) {
		redis.set("band-name:" + band.getBandName(), "1");
		for (Artist artist : band.getArtists()) {
			redis.set("artist-name:" + artist.getArtistName(), "1");
			for (String role : artist.getRoles()) {
				redis.set("role-name:" + role, "1");
			}
		}
		// for(String role: band.getArtists())
	}

	private void feedBandToNeo4j(PingBand band) throws JsonProcessingException {
		boolean newBand = false;
		boolean newArtist = false;
				
		if (band.redisPing()) {
			// Redis にバンドが存在する
			System.out.println(">> Redis にバンドが存在する");
			redis.expire(band.getCashKey(), expiry);
		} else if (band.neo4jPing()) {
			// Neoにのみバンドが存在する
			System.out.println(">> Neoにのみバンドが存在する");
			redis.setex(band.getCashKey(), expiry, "1");
		} else {
			// バンドが存在しない
			System.out.println(">> バンドが存在しない");
			neo4j.writeByCypherStatement("create (b:Band {name:\"" + band.getBandName() + "\"})");
			redis.setex(band.getCashKey(), expiry, "1");
			newBand = true;
		}

		for (Artist tmp : band.getArtists()) {
			PingArtist pingArtist = (PingArtist) tmp;

			if (pingArtist.redisPing()) {
				System.out.println(">> Redis にアーチストが存在する");
				redis.expire(pingArtist.getCashKey(), expiry);
			} else if (pingArtist.neo4jPing()) {
				System.out.println(">> Neoにのみアーチストが存在する");
				redis.setex(pingArtist.getCashKey(), expiry, "1");
			} else {
				System.out.println(">> アーチストが存在しない");
				neo4j.writeByCypherStatement("create (a:Artist {name:\"" + pingArtist.getArtistName() + "\"})");
				redis.setex(pingArtist.getCashKey(), expiry, "1");
				newArtist = true;
			}

			
			if(newBand || newArtist) {
				System.out.println(">>band->atrist リレーションを張る");
				System.out.println("match (b:Band {name:\"" 
								+ band.getBandName() +
								"\"}), (a:Artist {name: \"" 
								+ pingArtist.getArtistName()+
								"\"}) create (b)-[:MEMBER]->(a)");
			}
						
						
//			match (b:Band {name:"EY"}), (a:Artist {name: "e-ch"})
//			create (b)-[:MEMBER]->(a)

			// role をlookup or create する
			// artist - role リレーションを張る
		}

	}

}

class PingBand extends Band {
	private String cashKey;
	// public HashMap<String,String> ping() {
	// HashMap<String,String> result = new HashMap<>();
	// result.put("ping", "pong");
	// result.put("from","Redis");
	// return result;
	// }

	public boolean redisPing() {
		return Band.getRedis().get(cashKey) != null;
	}

	public boolean neo4jPing() throws JsonProcessingException {
		return (Band.getNeo4jDB().readByCypherStatement("match (b:Band {name: \"" + getBandName() + "\"}) return b")
				.size() != 0);

	}

	@Override
	public void setBandName(String name) {
		super.setBandName(name);
		cashKey = "lookup:band:name:" + getBandName();
	}

	public String getCashKey() {
		return cashKey;
	}

	public void setCashKey(String cashKey) {
		this.cashKey = cashKey;
	}

}

class PingArtist extends Artist {
	private String cashKey;

	@Override
	public void setArtistName(String name) {
		super.setArtistName(name);
		cashKey = "lookup:artist:name:" + getArtistName();
	}

	public boolean redisPing() {
		return Artist.getRedisDB().get(cashKey) != null;
	}

	public boolean neo4jPing() throws JsonProcessingException {
		return (Artist.getNeo4jDB()
				.readByCypherStatement("match (a:Artist {name: \"" + getArtistName() + "\"}) return a").size() != 0);

	}

	public String getCashKey() {
		return cashKey;
	}

	public void setCashKey(String cashKey) {
		this.cashKey = cashKey;
	}

}