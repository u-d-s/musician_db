package uds.birdmanbros.test.musician_db;

import org.bson.Document;

public interface MongoWatcher {
	void change(Document obj);
}
