package uds.birdmanbros.test.musician_db;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {
	public static void main(String[] args) {
		RedisToMongo2 app = new RedisToMongo2();
		app.run();
	}
}


 	