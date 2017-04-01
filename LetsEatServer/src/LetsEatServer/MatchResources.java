package LetsEatServer;

import java.util.Iterator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/matches")
public class MatchResources {
	
	@GET
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMatches(@PathParam("userId") String userId) {
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MATCHES");
    	
    	/* If the user does not exist, return null. */
    	if (doc.find(new Document("_id", userId)).first() == null) {
    		mongo.close();
    		return null;
    	}
    	
    	/* Get the user's document from the database. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	
    	/* Close the connection to the database. */
    	mongo.close();
    	
    	/* If the user exists, return all the user's information. */
    	return user.toJson();
    	
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void putMatches(@PathParam("userId") String userId, String matches) {
    	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(matches);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MATCHES");
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	
    	/* Update the user's matches. */
    	Iterator<JsonNode> matchValues = node.iterator();
    	Iterator<String> matchNames = node.fieldNames();
    	while (matchValues.hasNext()) {
    		JsonNode matchValue = matchValues.next();
    		String matchName = matchNames.next();
    		if (matchValue.isInt()) {
    			doc.updateOne(new Document("_id", userId), new Document("$set", new Document(matchName, matchValue.asInt())));
    		}
    	}
        
        /* Close connection to database. */
        mongo.close();
    
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void postMatches(@PathParam("userId") String userId, String matches) {
    	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(matches);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MATCHES");
    	
    	/* Create a new document in the collection if one has not been created yet. */
    	if (doc.find(new Document("_id", userId)).first() == null) {
    		 doc.insertOne(new Document("_id", userId));
    	}
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	
    	/* Update the user's matches. */
    	Iterator<JsonNode> matchValues = node.iterator();
    	Iterator<String> matchNames = node.fieldNames();
    	while (matchValues.hasNext()) {
    		JsonNode matchValue = matchValues.next();
    		String matchName = matchNames.next();
    		if (matchValue.isInt()) {
    			doc.updateOne(new Document("_id", userId), new Document("$set", new Document(matchName, matchValue.asInt())));
    		}
    	}
        
        /* Close connection to database. */
        mongo.close();
    
	}
	
	@DELETE
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void deleteMatches(@PathParam("userId") String userId) {
			
	    /* Connect to the MongoDB database. */
	    ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
	    MongoClient mongo = new MongoClient(adr);
	    MongoDatabase data = mongo.getDatabase("Users");
	    MongoCollection<Document> doc = data.getCollection("MATCHES");

	    /* Delete the document containing the user's information. */
	    doc.deleteOne(new Document("_id", userId));

	    /* Close connection to database. */
	    mongo.close();
	        
	}
}
