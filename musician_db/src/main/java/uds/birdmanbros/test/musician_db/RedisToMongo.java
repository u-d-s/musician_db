package uds.birdmanbros.test.musician_db;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.json.bind.*;

import org.bson.Document;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.mongodb.client.MongoCollection;

import uds.birdmanbros.test.musician_db.redis_to_mongo.Artist;
import uds.birdmanbros.test.musician_db.redis_to_mongo.Band;

public class RedisToMongo {

	public void run() {
		System.out.format("RedisToMongo>> run%n");
		
		try(RedisDB redis = new RedisDB();
				MongoDB mongo = new MongoDB()){

//		RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
//		StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
//		RedisCommands<String, String> redisSyncCommands = redisConnection.sync();

		// MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		// MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
		// MongoCollection<Document> mongoCollection =
		// mongoDatabase.getCollection("myTestCollection");

		String keyOfArtists_rex = "band:U*";

		Band.setRedisDB(redis);
		Band.setMongoDB(mongo);
		Artist.setRedisDB(redis);
		Band band = new Band();
		// band.setMongoCollection(mongoCollection);

		List<String> keyOfArtists = redis.keys(keyOfArtists_rex);

		int i = 0;
		for (String koa : keyOfArtists) {
			band.updateBand(koa);
			band.writeIntoMongoDB();

//			JsonbConfig config = new JsonbConfig().withNullValues(true).withFormatting(true);
			JsonbConfig config = new JsonbConfig().withFormatting(true);
			Jsonb jsonb = JsonbBuilder.create(config);

//			String[] aaa = { "a", "bc" };

			System.out.format("DEBUG>> %s%n", jsonb.toJson(band));
//			 System.out.format("%d DEBUG>> %s%n", i++,koa);
		}
		
		}catch(Exception x) {
			x.printStackTrace();
		}

		// mongoClient.close();

	}
}