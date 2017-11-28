package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.util.LinkedList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;

public class MongoDB implements Closeable {
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> mongoCollection;
	private String host;
	private int port;
	private String db;
	private String collection;
	private int bulkWriteQueueMaxSize;
	private LinkedList<WriteModel<Document>> bulkWriteQueue;
	
	
	
	public void pushIntoBulkWriteQueue(Document document) {
		System.out.format("before>> queue size %d%n", bulkWriteQueue.size());
//		JsonbConfig config = new JsonbConfig().withFormatting(true).withNullValues(true);
//		Jsonb jsonb = JsonbBuilder.create(config);
//		System.out.format("DEBUG>> %s%n", jsonb.toJson(bulkWriteQueue));
//		
		
		if(bulkWriteQueue.size() < bulkWriteQueueMaxSize) {
			bulkWriteQueue.addLast(new InsertOneModel<Document>(document));
		}else {
			flushBulkWriteQueue();
		}
	}
	
	public void flushBulkWriteQueue() {
		System.out.format(">> flush%n");
		mongoCollection.bulkWrite(bulkWriteQueue, new BulkWriteOptions().ordered(true)); 
		bulkWriteQueue.clear();
	}
	
	
	public int getBulkWriteQueueMaxSize() {
		return bulkWriteQueueMaxSize;
	}
	public void setBulkOperations(int bulkWriteQueueMaxSize) {
		this.bulkWriteQueueMaxSize = bulkWriteQueueMaxSize;
	}

	
	@Override
	public void close() {
		mongoClient.close();
		System.out.format("MongoDB has closed.%n");
	}
	
	public MongoDB() {
		host = "localhost";
		port = 27017;
		db = "7d7w";
		collection = "musicians";
		
		mongoClient = new MongoClient(host,port);
		mongoDatabase = mongoClient.getDatabase(db);
		mongoCollection = mongoDatabase.getCollection(collection);
		
		bulkWriteQueue = new LinkedList<WriteModel<Document>>();
		bulkWriteQueueMaxSize = 16;
	}
}
