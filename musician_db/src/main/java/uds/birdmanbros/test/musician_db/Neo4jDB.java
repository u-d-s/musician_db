package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class Neo4jDB implements Closeable {
	private Driver driver;
 	private Session session;
 	private String uri = "bolt://localhost:7687";
 	private String user = "neo4j";
 	private String password = "neo4j";
 	private ObjectMapper om;
 	
 	
 	List<Map<String,Object>> readByCypherStatement(String statement) throws JsonProcessingException {
 		List<Map<String, Object>> results_l = new LinkedList<>();
 		StatementResult results_it = session.readTransaction( new TransactionWork<StatementResult>(){
 	    	 @Override
             public StatementResult execute(Transaction tx){
 	    		return tx.run(statement);
             }
         } );
 		while (results_it.hasNext()){
 			results_l.add(results_it.next().asMap());
 	    }
 		
 		return results_l;
 		
 	}
 	
	void writeByCypherStatement(String statement) throws JsonProcessingException {

		session.readTransaction(new TransactionWork<StatementResult>() {
			@Override
			public StatementResult execute(Transaction tx) {
				return tx.run(statement);
			}
		});

	}
 	
 	
 	
 	@Override
 	public void close() {
 		session.close();
 		driver.close();
 		
 		System.out.format("Neo4jDB has closed.%n");
 	}
 	
 	public Neo4jDB() {
 		driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
 		session = driver.session();
 		om = new ObjectMapper();
 		om.enable(SerializationFeature.INDENT_OUTPUT);
 	}

}
