package uds.birdmanbros.test.musician_db;


public class App 
{
    public static void main( String[] args )
    {
    	TsvToRedis application = new TsvToRedis();
    	application.run();
    }
    
}
 	
//        System.out.println( "alo!" );
//        
//    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
//    	StatefulRedisConnection<String, String> connection = redisClient.connect();
//    	RedisCommands<String, String> syncCommands = connection.sync();
//        
//        try(TsvFile tsvFile = new TsvFile("src\\main\\resources\\group_membership_short.tsv")){
//            System.out.format("current>> %s%ntsv>> %s%n", tsvFile.getCurrentPath(), tsvFile.getAbsolutePath());
//            
//            tsvFile.setCharset("UTF-8");
//            
//            tsvFile.readHeader();
//            
//            System.out.format("header>> %s%n", String.join(",", tsvFile.getHeader()));           
//            System.out.format("maxColumn>> %d%n", tsvFile.getMaxColumn());	
//           
//
//        	String[] data;
//        	App.LineDataSet lineDataSet = new App.LineDataSet();
//        	long counter_isValid=0;
//        	long counter_hasRoles=0;
//            try {
//            	while(true) {
////            		System.out.format("tcv> %s%n", String.join(",", tsvFile.readLine()));
//            		data = tsvFile.readLine();
//            		lineDataSet.setData(data[3],data[2],data[4]);
//            		System.out.format("1>> %s%n", lineDataSet.toString());
//            		System.out.format("2>> %s%n", lineDataSet.hasRoles());
//            		
//            		if(lineDataSet.isValid()) {
//            			syncCommands.sadd(lineDataSet.getKeyOfArtist(), lineDataSet.getArtist());
////            			System.out.format("DEBUG>> %s %s%n", lineDataSet.getKeyOfArtist(), lineDataSet.getArtist());
//            			counter_isValid++;
//            		}
//            		
//            		if(lineDataSet.hasRoles()) {
//            			String[] roles = lineDataSet.getRoles();
//            			for(String role: roles)
//                			syncCommands.sadd(lineDataSet.getKeyOfRoles(), role);
////            				System.out.format("DEBUG>> %s %s%n", lineDataSet.getKeyOfRoles(), role);
//            			counter_hasRoles++;
//            		}
//            		
////            		artist = data[2];
////            		band = data[3];
////            		roles = data[4];
// 
//            	}
//            }catch(EOFException eof) {
//            	System.out.format("Ive finished reading the tsv file%n");
//            	System.out.format("  %s%n",  eof.toString());
//            }catch(IOException x) {
//            	throw x;
//            }
//            System.out.format("counter_isValid>> %d%n",counter_isValid);
//            System.out.format("counter_hasRoles>> %d%n",counter_hasRoles);
//            
//    
//        }catch(IOException x) {
//			System.err.format("IOException: %s%n", x);
//        }
//        
//
//    	connection.close();
//    	redisClient.shutdown();
//
//        /*
//		Path current_path = Paths.get(".");
//		System.out.format(">>> %s%n", current_path.toAbsolutePath());
//		
//		Path tsv_file_path = Paths.get("src\\main\\resources\\group_membership_short.tsv");
//		System.out.format(";->  %s%n", tsv_file_path.toAbsolutePath());
//		
//		try (BufferedReader reader = Files.newBufferedReader(tsv_file_path, Charset.forName("UTF-8"))) {
//		    String line = null;
//		    while ((line = reader.readLine()) != null) {
//		    	System.out.format(";->  %s%n",line);
//		    }
//		} catch (IOException x) {
//			System.err.format("IOException: %s%n", x);
//		}
//         */
//        /*
//        
//    	RedisClient redisClient = RedisClient.create("redis://localhost:6379/15");
//    	StatefulRedisConnection<String, String> connection = redisClient.connect();
//    	RedisCommands<String, String> syncCommands = connection.sync();
//
////    	syncCommands.set("lettuce", "Hello, Redis!");
////    	String value;
////    	value = syncCommands.get("lettuce");
////    	System.out.format("value= %s%n", value);
//    	syncCommands.sadd("slettuce", "alo");
//    	Set<String> svalue;
//    	svalue = syncCommands.smembers("slettuce");
//    	System.out.format("value= %s%n", svalue.toString());
//
//    	connection.close();
//    	redisClient.shutdown();
//    	*/
//    	
//    }
//
//
//    
//    static private class LineDataSet {
//    	private String band;
//    	private Artist artist;
//    	
//    	public boolean isValid() {
//    		return !( band.equals("") || (artist.getArtist().equals("")) );
//    	}
//    	
//    	public boolean hasRoles() {
//    		return artist.hasRoles();
//    	}
//    	
//    	public String getKeyOfArtist() {
//    		return "band:" +band;
//    	}
//    	
//    	public String getKeyOfRoles() {
//    		return "artist:"+band+":"+artist.getArtist();
//    	}
//    	
//    	public void setData(String b, String a, String r) {
//    		band = b;
//    		artist.setData(a, r);
//    	}
//    	
//    	public String getArtist() {
//    		return artist.getArtist();
//    	}
//    	
//    	public String[] getRoles() {
//    		return artist.getRoles();
//    	}
//    	
//    	
//    	public String getBand() { return band; }
//    	
//    	public String toString() {
//    		return "band: "+band+"| "+artist.toString();
//    	}
//    	
//
//    	public LineDataSet() {
//    		artist = new Artist();
//    	}
//    }
//    
//    static private class Artist {
//    	private String artist;
//    	private String[] roles;
//    	
//    	
//    	public boolean hasRoles() {
//    		return !(Arrays.equals(roles, new String[]{}));
//    	}
//    	
//    	public void setData(String a,String r) {
//    		artist = a;
//    		roles = r.equals("") ? new String[] {} : r.split(",",0);
//    		for(int i=0;i<roles.length;i++) { roles[i] = roles[i].trim(); }
//    	}
//    	
//    	
//    	
//    	public String getArtist() { return artist; }
//    	
//    	public String[] getRoles() { return roles;}
//    	
//    	public String toString() {
//    		return "artist: "+artist+"| roles: "+String.join("/", roles);
//    	}
//    	
//    	 ***************************** */
//    	
//    }
//    
//
//}
