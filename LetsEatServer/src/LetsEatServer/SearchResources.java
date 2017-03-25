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
		
		ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
		MongoClient client = new MongoClient(adr);
		MongoDatabase database = client.getDatabase("Users");
		MongoCollection<Document> collection = database.getCollection("USERS");
		FindIterable<Document> users = collection.find(new Document("age", age));
		Document doc = new Document();
		MongoCursor<Document> cursor = users.iterator();
		Document caller = collection.find(new Document("_id", userId)).first(); 
		//while (cursor.hasNext()) {
			Document user =  cursor.next();
			return callerLoc.get(0);
			/* if (Search.getDistance(callerLoc, userLoc) <= ((double) user.get("maxRange") + (double) caller.get("maxRange"))) {
				doc.append(user.getString("_id"), user);
			}
		}
		client.close();
		return doc.toJson(); */
		
	}

}
