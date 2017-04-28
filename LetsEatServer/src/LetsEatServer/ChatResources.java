package LetsEatServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/chat/{userId}")
public class ChatResources {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChatHistory(@PathParam("userId") String userId) {
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("CHATS");
    	
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void putChatHistory(@PathParam("userId") String userId, String chat) {
	   	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(chat);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("CHATS");
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	
    	/* Update the user's chat histories. */
    	Iterator<JsonNode> chats = node.iterator();
    	Iterator<String> chatNames = node.fieldNames();
    	while (chats.hasNext()) {
    		JsonNode chatMessages = chats.next();
    		String chatName = chatNames.next();
    		if (chatName.compareTo("_id") != 0) {
                Iterator<JsonNode> arrayValues = chatMessages.elements();
                List<String> values = new ArrayList<String>();
                while (arrayValues.hasNext()) {
             	    JsonNode value = arrayValues.next();
            	    values.add(value.textValue());
                }
    			doc.updateOne(new Document("_id", userId), new Document("$set", new Document(chatName, values)));
    		}
    	}
        
        /* Close connection to database. */
        mongo.close();

	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void postChatHistory(@PathParam("userId") String userId, String chat) {
	   	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(chat);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("CHATS");
    	
    	/* Create a new document in the collection if one has not been created yet. */
    	if (doc.find(new Document("_id", userId)).first() == null) {
    		 doc.insertOne(new Document("_id", userId));
    	}
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	
    	/* Update the user's chat histories. */
    	Iterator<JsonNode> chats = node.iterator();
    	Iterator<String> chatNames = node.fieldNames();
    	while (chats.hasNext()) {
    		JsonNode chatMessages = chats.next();
    		String chatName = chatNames.next();
    		if (chatName.compareTo("_id") != 0) {
                Iterator<JsonNode> arrayValues = chatMessages.elements();
                List<String> values = new ArrayList<String>();
                while (arrayValues.hasNext()) {
             	    JsonNode value = arrayValues.next();
            	    values.add(value.textValue());
                }
    			doc.updateOne(new Document("_id", userId), new Document("$set", new Document(chatName, values)));
    		}
    	}
        
        /* Close connection to database. */
        mongo.close();

	}
	
	@DELETE
    @Produces(MediaType.APPLICATION_JSON)
	public void deleteUser(@PathParam("userId") String userId) {
		
	    /* Connect to the MongoDB database. */
	    ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
	    MongoClient mongo = new MongoClient(adr);
	    MongoDatabase data = mongo.getDatabase("Users");
	    MongoCollection<Document> doc = data.getCollection("CHATS");

	    /* Delete the document containing the user's information. */
	    doc.deleteOne(new Document("_id", userId));

	    /* Close connection to database. */
	    mongo.close();
	        
	}
}
