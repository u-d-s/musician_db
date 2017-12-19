package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MongoToRedisAndNeo4j implements MongoWatcher, Closeable {
	private RedisDB redis;
	private Neo4jDB neo4j;
	private long expiry;
	private long receivedDocuments;
	private long interestingBands;
	private long createdBands;
	private long createdArtists;
	private long createdRoles;

//	public void run() {
//		System.out.format(">> MogoToRedisAndNeo4j%n");
//
//		expiry = 60;
//
//		mongo = new MongoDB();
//		redis = new RedisDB("redis://localhost:6379/", 0);
//		neo4j = new Neo4jDB();
//		Band.setMongoDB(mongo);
//		Band.setRedisDB(redis);
//		Band.setNeo4jDB(neo4j);
//		Artist.setRedisDB(redis);
//		Artist.setNeo4jDB(neo4j);
//		try {
//			mongo.watchedBy(this);
//
//			Document bandDoc = new Document();
//			Document artistDoc1 = new Document();
//			Document artistDoc2 = new Document();
//
//			artistDoc1.append("artistName", "Nishikawa")
//			.append("roles", Arrays.asList("drugger", "keyboard"));
//			artistDoc2.append("artistName", "the other one")
//			.append("roles", Arrays.asList("base","comedian"));
//
//			bandDoc.append("bandName",  "Dcome")
//			.append("artists", Arrays.asList(artistDoc1, artistDoc2));
//
//			mongo.pushIntoBulkWriteQueue(bandDoc);			
//			mongo.flushBulkWriteQueue();
//		} catch (Exception x) {
//			x.printStackTrace();
//		} finally {
//			mongo.close();
//			redis.close();
//			neo4j.close();
//		}
//
//	}

	public void change(Document doc) {
		// ObjectMapper mapper = new
		// ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		receivedDocuments++;
		PingBand pingBand = new PingBand(doc);
		System.out.println("DEBUG>> " + doc.toJson());

		if (pingBand.isInteresting() == false) {
			System.out.format(">> no %s%n", pingBand.doesNotHave());
		} else {
			interestingBands++;

			try {
				feedBandToRedis(pingBand);
				feedBandToNeo4j(pingBand);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			// }
			// }
		}
		
//		System.out.format(">>> mTRN-change() has finished.%n"
//				+ " receivedDocuments %d%n"
//				+ " interestingBands %d%n"
//				+ " createdBands %d%n"
//				+ " createdArtists %d%n"
//				+ " createdRoles %d%n",
//					receivedDocuments,interestingBands,createdBands,createdArtists,createdRoles
//					);

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

		try {
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
				neo4j.writeByCypherStatement("create (b:Band {name:\"" + band.getBandName().replace("\"","\\\"") + "\"})");
				redis.setex(band.getCashKey(), expiry, "1");
				newBand = true;
				
				createdBands++;
			}

			for (Artist tmp : band.getArtists()) {
				PingArtist pingArtist = (PingArtist) tmp;
				boolean newArtist = false;

				if (pingArtist.redisPing()) {
					System.out.println(">> Redis にアーチストが存在する");
					redis.expire(pingArtist.getCashKey(), expiry);
				} else if (pingArtist.neo4jPing()) {
					System.out.println(">> Neoにのみアーチストが存在する");
					redis.setex(pingArtist.getCashKey(), expiry, "1");
				} else {
					System.out.println(">> アーチストが存在しない");
					neo4j.writeByCypherStatement("create (a:Artist {name:\"" + pingArtist.getArtistName().replace("\"","\\\"") + "\"})");
					redis.setex(pingArtist.getCashKey(), expiry, "1");
					newArtist = true;
					
					createdArtists++;
				}

				if (newBand || newArtist) {
					System.out.println(">>band->atrist リレーションを張る");
					// System.out.println("match (b:Band {name:\""
					// + band.getBandName() +
					// "\"}), (a:Artist {name: \""
					// + pingArtist.getArtistName()+
					// "\"}) create (b)-[:MEMBER]->(a)");
					neo4j.writeByCypherStatement(
							"match (b:Band {name:\"" + band.getBandName().replace("\"","\\\"") + "\"}), (a:Artist {name: \""
									+ pingArtist.getArtistName().replace("\"","\\\"") + "\"}) create (b)-[:MEMBER]->(a)");
				}

				if (pingArtist.hasRole() == false) {
					System.out.println("no role");
				} else {
					for (String role : pingArtist.getRoles()) {
						boolean newRole = false;
						String cashKey = "lookup:role:name:" + role;
						if (redis.get(cashKey) != null) {
							System.out.println(">> Redis にロールが存在する");
							redis.expire(cashKey, expiry);
						} else if (neo4j.readByCypherStatement("match (r:Role {name: \"" + role.replace("\"","\\\"") + "\"}) return r")
								.size() != 0) {
							System.out.println(">> Neoにのみロールが存在する");
							redis.setex(cashKey, expiry, "1");
						} else {
							System.out.println(">> ロールが存在しない");
							// System.out.println("create (r:Role {name:\"" + role + "\"})");
							neo4j.writeByCypherStatement("create (r:Role {name:\"" + role.replace("\"","\\\"") + "\"})");
							redis.setex(cashKey, expiry, "1");
							newRole = true;
							
							createdRoles++;
						}

						// artist - role リレーションを張る
						if (newArtist || newRole) {
							System.out.println(">>atrist->role リレーションを張る");
							// System.out.println(
							// "match (a:Artist {name:\"" + pingArtist.getArtistName() + "\"}), (r:Role
							// {name: \""
							// + role + "\"}) create (a)-[:PLAYS]->(r)");
							neo4j.writeByCypherStatement("match (a:Artist {name:\"" + pingArtist.getArtistName().replace("\"","\\\"")
									+ "\"}), (r:Role {name: \"" + role.replace("\"","\\\"") + "\"}) create (a)-[:PLAYS]->(r)");
						}
					}
				}
			}

		} catch (Exception x) {
			x.printStackTrace();
		}
	}
	
	public void close() {
		redis.close();
		neo4j.close();
	}


	
	public long getExpiry() {
		return expiry;
	}

	public long getReceivedDocuments() {
		return receivedDocuments;
	}

	public long getInterestingBands() {
		return interestingBands;
	}

	public long getCreatedBands() {
		return createdBands;
	}

	public long getCreatedArtists() {
		return createdArtists;
	}

	public long getCreatedRoles() {
		return createdRoles;
	}

	public MongoToRedisAndNeo4j() {
		expiry = 60;
		receivedDocuments = 0;
		interestingBands = 0;
		createdBands = 0;
		createdArtists = 0;
		createdRoles = 0;


		redis = new RedisDB("redis://localhost:6379/", 14);
		neo4j = new Neo4jDB();
		
//		Band.setMongoDB(mongo);
		Band.setRedisDB(redis);
		Band.setNeo4jDB(neo4j);
		Artist.setRedisDB(redis);
		Artist.setNeo4jDB(neo4j);
	}
}

class PingBand extends Band {
	private String cashKey;
	private boolean hasRole;
	private String missingItem;
	// public HashMap<String,String> ping() {
	// HashMap<String,String> result = new HashMap<>();
	// result.put("ping", "pong");
	// result.put("from","Redis");
	// return result;
	// }
	
	public boolean isInteresting() {
		return hasRole;
	}
	
	public String doesNotHave() {
		return missingItem;
	}

	public boolean redisPing() {
		return Band.getRedis().get(cashKey) != null;
	}

	public boolean neo4jPing() throws JsonProcessingException, UnsupportedEncodingException {
		return (Band.getNeo4jDB().readByCypherStatement("match (b:Band {name: \"" + getBandName().replace("\"","\\\"") + "\"}) return b")
				.size() != 0);

	}
	
	public boolean hasArtist() {
		return super.getArtists().size() > 0 ? true : false;
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

	public PingBand(Document doc) {
		super();

		if (doc.getString("_id") == null || doc.getString("_id").isEmpty()) {
			hasRole = false;
			missingItem = "bandName";
		} else {
			this.setBandName(doc.getString("_id"));

			if ((List<Document>) doc.get("artists") == null || ((List<Document>) doc.get("artists")).size() == 0) {
				hasRole = false;
				missingItem = "artists";
			} else {
				hasRole = false;
				missingItem = "roles";
				for (Document docArtist : (List<Document>) doc.get("artists")) {
					if (docArtist.get("roles") != null && ((List<String>)docArtist.get("roles")).size() > 0) {
						hasRole = true;
						missingItem = "with every Item";
						super.addArtist((Artist) new PingArtist(docArtist));
					}
				}
			}
		}
	}

}

class PingArtist extends Artist {
	private String cashKey;
	
	public boolean hasRole() {
		return super.getRoles().size() > 0 ? true : false;
	}

	@Override
	public void setArtistName(String name) {
		super.setArtistName(name);
		cashKey = "lookup:artist:name:" + getArtistName();
	}

	public boolean redisPing() {
		return Artist.getRedisDB().get(cashKey) != null;
	}

	public boolean neo4jPing() throws JsonProcessingException, UnsupportedEncodingException {
		return (Artist.getNeo4jDB()
				.readByCypherStatement("match (a:Artist {name: \"" + getArtistName().replace("\"","\\\"") + "\"}) return a").size() != 0);
	}

	public String getCashKey() {
		return cashKey;
	}

	public void setCashKey(String cashKey) {
		this.cashKey = cashKey;
	}

	public PingArtist(Document doc) {
		super();

		this.setArtistName(doc.getString("name"));

		List<String> list = (List<String>)doc.get("roles");
		if (list == null || list.size() == 0) {
			System.out.println("no role in PingArtist(Doc)");
		} else {
			for (String role : list) {
				super.addRole(role);
			}
		}

	}
}

