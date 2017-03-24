package LetsEatServer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.*;
import java.util.*;

@Path ("/search/age/{age}")
public class SearchResources {
	
	@GET
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List getSearch(@PathParam ("userId") String userId, @PathParam ("age") int age) {
		
		ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
		MongoClient client = new MongoClient(adr);
		MongoDatabase database = client.getDatabase("Users");
		MongoCollection<Document> collection = database.getCollection("USERS");
		FindIterable<Document> users = collection.find(new Document("age", age));
		client.close();
		List list = new ArrayList();
		MongoCursor<Document> cursor = users.iterator();
		while (cursor.hasNext()) {
			Document user =  cursor.next();
			list.add(user);
		}
		return list;
		
	}

}
