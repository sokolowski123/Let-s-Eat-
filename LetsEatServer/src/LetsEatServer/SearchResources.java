package LetsEatServer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.*;
import java.util.*;

@Path ("/search/user/{userId}/age/{age}")
public class SearchResources {
	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getSearch(@PathParam ("userId") String userId, @PathParam ("age") int age) {
		
		/* Connect to the mongoDb database. */
		ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
		MongoClient client = new MongoClient(adr);
		MongoDatabase database = client.getDatabase("Users");
		MongoCollection<Document> collection = database.getCollection("USERS");
		
		/* Find all the users at the given age. */
		FindIterable<Document> users = collection.find(new Document("age", age));
		
		/* Create the document that while contain all the users at the given age. */
		Document doc = new Document();
		
		/* Find the user that is calling for the search. */
		Document caller = collection.find(new Document("_id", userId)).first(); 
		
		/* Iterate through the users with a given age. */
		MongoCursor<Document> cursor = users.iterator();
		while (cursor.hasNext()) {
			Document user =  cursor.next();
			
			/* Don't return the caller in the search. */
			if (user.getString("_id") != caller.getString("_id")) {
			
			    /* Only return the users who are within the calling user's range. */
			    List<Double> callerLoc = (List<Double>) caller.get("location");
			    List<Double> userLoc = (List<Double>) user.get("location");
				if (Search.getDistance(callerLoc, userLoc) <= Double.parseDouble(Integer.toString(((int) user.get("maxRange") + (int) caller.get("maxRange"))))) {
				    
					/* Check that the user and caller have not interacted before. */
					MongoCollection<Document> matches = database.getCollection("MATCHES");
					Document callerMatches = matches.find(new Document("_id", userId)).first();
					if (callerMatches.get(user.getString("_id")) != null) {
						
						/* If user passes through statements, then add is a possible match. */
					    doc.append(user.getString("_id"), user);
					    
					}
					
				}
				
			}
			
		}
		
		/* Close connection to database. */
		client.close();
		
		return doc.toJson();
		
	}

}
