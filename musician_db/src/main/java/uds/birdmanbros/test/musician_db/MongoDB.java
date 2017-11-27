package uds.birdmanbros.test.musician_db;

import java.io.Closeable;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB implements Closeable {
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> mongoCollection;
	private String host;
	private int port;
	private String db;
	private String collection;
	
	
	@Override
	public void close() {
		mongoClient.close();
		System.out.format("MongoDB has closed.%n");
	}
	
	public MongoDB() {
		host = "localhost";
		port = 27017;
		db = "7d7w";
		collection = "musician";
		
		mongoClient = new MongoClient(host,port);
		mongoDatabase = mongoClient.getDatabase(db);
		mongoCollection = mongoDatabase.getCollection(collection);
	}
}
