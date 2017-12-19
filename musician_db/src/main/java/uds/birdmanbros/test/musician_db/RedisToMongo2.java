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

public class RedisToMongo2 {

	public void run() {
		System.out.format("RedisToMongo 2 >> run%n");

		try (RedisDB redis = new RedisDB("redis://localhost:6379/", 15);
				MongoDB mongo = new MongoDB("localhost", 27017, "7d7w", "musicians_test");
				MongoToRedisAndNeo4j mToRN = new MongoToRedisAndNeo4j()) {

			mongo.watchedBy(mToRN);
			String keyOfArtists_rex = "band:*";

			Band.setRedisDB(redis);
			Artist.setRedisDB(redis);
//			mongo.setBulkOperations(1);
			Band.setMongoDB(mongo);
			Band band = new Band();

			List<String> keyOfArtists = redis.keys(keyOfArtists_rex);

			int i = 0;
			for (String koa : keyOfArtists) {
				band.updateBand(koa);
				mongo.pushIntoBulkWriteQueue(band.createDocument());
			}
			
			mongo.flushBulkWriteQueue();
			System.out.format(">> total processedDocuments are %d%n", mongo.getProcessedDocuments());
			
			System.out.format(">>> RedisToMongo2-run() has finished.%n"
					+ " receivedDocuments %d%n"
					+ " interestingBands %d%n"
					+ " createdBands %d%n"
					+ " createdArtists %d%n"
					+ " createdRoles %d%n",
					mToRN.getReceivedDocuments(),mToRN.getInterestingBands(),mToRN.getCreatedBands(),mToRN.getCreatedArtists(),mToRN.getCreatedRoles()
						);

		} catch (Exception x) {
			x.printStackTrace();
		}
		
	}
}