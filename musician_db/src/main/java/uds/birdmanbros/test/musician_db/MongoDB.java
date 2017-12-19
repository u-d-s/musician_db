package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.util.Arrays;
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
	private LinkedList<InsertOneModel<Document>> bulkWriteQueue;
//	private LinkedList<WriteModel<Document>> bulkWriteQueue;
	private long processedDocuments;
	private int printProcessedDocumentsEvery;
	private MongoWatcher watcher;
	
	
	
	public void pushIntoBulkWriteQueue(Document document) {
//		System.out.format("before>> queue size %d%n", bulkWriteQueue.size());
//		JsonbConfig config = new JsonbConfig().withFormatting(true).withNullValues(true);
//		Jsonb jsonb = JsonbBuilder.create(config);
//		System.out.format("DEBUG>> %s%n", jsonb.toJson(bulkWriteQueue));
//		
		
		if(bulkWriteQueue.size() >= bulkWriteQueueMaxSize) {
			flushBulkWriteQueue();
		}
		bulkWriteQueue.addLast(new InsertOneModel<Document>(document));
	}
	
	public void flushBulkWriteQueue() {
//		System.out.format(">> flush%n");
		mongoCollection.bulkWrite(bulkWriteQueue, new BulkWriteOptions().ordered(true));
		for(InsertOneModel<Document> model: bulkWriteQueue) {
			change(model.getDocument());
		}
		processedDocuments += bulkWriteQueue.size();
		bulkWriteQueue.clear();
		
		printProcessedDocuments();
	}
	
	private void printProcessedDocuments() {
		if(processedDocuments % printProcessedDocumentsEvery < bulkWriteQueueMaxSize) {
			System.out.format("processedDocuments>> %d%n", processedDocuments);
		}
	}
	
	public void watchedBy(MongoWatcher watcher) {
		this.watcher = watcher;
	}
	
	private void change(Document doc) {
		
//		PingBand band = new PingBand();
//		
//		LinkedList<String> roles = new LinkedList<>();
//		roles.add("band master");
//		roles.add("vocal");
//		roles.add("the one");
//		
//		PingArtist artist = new PingArtist();
//		artist.setArtistName("James Brown Jr6");
//		artist.setRoles(roles);
//		LinkedList<Artist> artists = new LinkedList<>();
//		artists.add(artist);
//		
//			
//		roles = new LinkedList<>();
//		roles.add("MC");
//		
//		artist = new PingArtist();
//		artist.setArtistName("a brother Jr5");
//		artist.setRoles(roles);
//		artists.add(artist);
//		
//		band.setArtists(artists);
//		
//		band.setBandName("OB JBs6");
		
//		Document bandDoc = new Document();
//		Document artistDoc1 = new Document();
//		Document artistDoc2 = new Document();
//		
//		artistDoc1.append("artistName", "Nishikawa")
//					.append("roles", Arrays.asList("drugger", "keyboard"));
//		artistDoc2.append("artistName", "the other one")
//					.append("roles", Arrays.asList("base","comedian"));
//		
//		bandDoc.append("bandName",  "Dcome")
//		.append("artists", Arrays.asList(artistDoc1, artistDoc2));

		watcher.change(doc);
	}
	
	
	
	
	public int getBulkWriteQueueMaxSize() {
		return bulkWriteQueueMaxSize;
	}
	public void setBulkOperations(int bulkWriteQueueMaxSize) {
		this.bulkWriteQueueMaxSize = bulkWriteQueueMaxSize;
	}
	public long getProcessedDocuments() {
		return processedDocuments;
	}
	public void setProcessedDocuments(long processedDocuments) {
		this.processedDocuments = processedDocuments;
	}

	
	
	@Override
	public void close() {
		mongoClient.close();
		System.out.format("MongoDB has closed; %s %d %s %s %n", host, port, db, collection);
	}
	
	public MongoDB() {
		this("localhost", 27017, "7d7w", "musicians");
	}
	
	public MongoDB(String h, int p, String d, String c) {
		host = h;
		port = p;
		db = d;
		collection = c;
		
		mongoClient = new MongoClient(host,port);
		mongoDatabase = mongoClient.getDatabase(db);
		mongoCollection = mongoDatabase.getCollection(collection);
		
		bulkWriteQueue = new LinkedList<InsertOneModel<Document>>();
		bulkWriteQueueMaxSize = 64;
		processedDocuments = 0;
		printProcessedDocumentsEvery = 100;
	}
}
