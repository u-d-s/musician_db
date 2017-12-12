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

public class RedisToMongo {

	public void run() {
		System.out.format("RedisToMongo>> run%n");

		try (RedisDB redis = new RedisDB(); MongoDB mongo = new MongoDB()) {

			// RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
			// StatefulRedisConnection<String, String> redisConnection =
			// redisClient.connect();
			// RedisCommands<String, String> redisSyncCommands = redisConnection.sync();

			// MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			// MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
			// MongoCollection<Document> mongoCollection =
			// mongoDatabase.getCollection("myTestCollection");

//			JsonbConfig config = new JsonbConfig().withFormatting(true);
//			config.withNullValues(true);
//			Jsonb jsonb = JsonbBuilder.create(config);

			String keyOfArtists_rex = "band:*";

			Band.setRedisDB(redis);
			Artist.setRedisDB(redis);
			// mongo.setBulkOperations(50);
			Band.setMongoDB(mongo);
			Band band = new Band();
			// band.setMongoCollection(mongoCollection);

			List<String> keyOfArtists = redis.keys(keyOfArtists_rex);

//			System.out.format("DEBUG>> %s%n", jsonb.toJson(mongo));
			int i = 0;
			for (String koa : keyOfArtists) {
				band.updateBand(koa);
				mongo.pushIntoBulkWriteQueue(band.createDocument());
//				System.out.format(">>DEBUG i %d%n", i++);
				// System.out.format("DEBUG2>> %s%n", jsonb.toJson(band.createDocument()));

				// JsonbConfig config = new
				// JsonbConfig().withNullValues(true).withFormatting(true);

				// String[] aaa = { "a", "bc" };

				// System.out.format("DEBUG>> %s%n", jsonb.toJson(band));
				// System.out.format("%d DEBUG>> %s%n", i++,koa);
			}
			
			mongo.flushBulkWriteQueue();
			System.out.format(">> total processedDocuments are %d%n", mongo.getProcessedDocuments());

		} catch (Exception x) {
			x.printStackTrace();
		}
		// mongoClient.close();

	}
}