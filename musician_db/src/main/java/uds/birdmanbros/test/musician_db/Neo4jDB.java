package uds.birdmanbros.test.musician_db;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
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
 	
 	
 	List<Map<String,Object>> readByCypherStatement(String statement) throws JsonProcessingException, UnsupportedEncodingException {
 		byte[] bytes = statement.getBytes("UTF-8");
 		String statement_utf8 = new String(bytes,"UTF-8");
 		
 		List<Map<String, Object>> results_l = new LinkedList<>();
 		StatementResult results_it = session.readTransaction( new TransactionWork<StatementResult>(){
 	    	 @Override
             public StatementResult execute(Transaction tx){
 	    		return tx.run(statement_utf8);
             }
         } );
 		while (results_it.hasNext()){
 			results_l.add(results_it.next().asMap());
 	    }
 		
 		return results_l;
 		
 	}
 	
	void writeByCypherStatement(String statement) throws JsonProcessingException, UnsupportedEncodingException {
 		byte[] bytes = statement.getBytes("UTF-8");
 		String statement_utf8 = new String(bytes,"UTF-8");


		session.readTransaction(new TransactionWork<StatementResult>() {
			@Override
			public StatementResult execute(Transaction tx) {
				return tx.run(statement_utf8);
			}
		});

	}
 	
 	
 	
 	@Override
 	public void close() {
 		session.close();
 		driver.close();
 		
 		System.out.format("Neo4jDB has closed; %s%n", uri);
 	}
 	
 	public Neo4jDB() {
 		driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
 		session = driver.session();
 		om = new ObjectMapper();
 		om.enable(SerializationFeature.INDENT_OUTPUT);
 	}

}
