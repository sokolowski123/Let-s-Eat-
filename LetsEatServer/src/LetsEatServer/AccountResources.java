package LetsEatServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/account")
public class AccountResources {
	
	@GET
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String compareAccount(@PathParam("userId") String userId /* , String account */) {
		
		/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	/* ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(account);
    	} catch (Exception e) {
    		return false;
    	} */
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("ACCOUNTS");
    	
    	/* Check that the user has an account. */
    	if (doc.find(new Document("_id", userId)) == null) {
    		mongo.close();
    		return null;
    	}
    	
    	/* Get the calling user's account information. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	
    	/* Compare the given password with the password that is stored in the database. */
    	try {
    		PassHash.verifyPass(/* node.get("password").textValue()*/ "hello", user.getString("password"));
    	} catch (Exception e) {
    		mongo.close();
    		return null;
    	}
    	
    	/* Close the connection to the database. */
    	mongo.close();
    	
    	return "What!";
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void changeAccount(@PathParam("userId") String userId, String account) {
		
		/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(account);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("ACCOUNTS");
    	
    	/* Hash the password that will be put in the database. */
    	String hash;
    	try {
    	    hash = PassHash.createHash(node.get("password").textValue());
    	} catch (Exception e) {
    		mongo.close();
    		return;
    	}
    	
    	/* Set the given password, now hashed, into the database. */
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("password", hash)));
    	
    	/* Close the connection to the database. */
    	mongo.close();
		
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void changeProfile(@PathParam("userId") String userId, String account) {
		
		/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(account);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("ACCOUNTS");
    	
    	if (doc.find(new Document("_id", userId)).first() == null) {
   		     doc.insertOne(new Document("_id", userId));
   	    }
    	
    	/* Hash the password that will be put in the database. */
    	String hash;
    	try {
    	    hash = PassHash.createHash(node.get("password").textValue());
    	} catch (Exception e) {
    		mongo.close();
    		return;
    	}
    	
    	/* Set the given password, now hashed, into the database. */
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("password", hash)));
    	
    	/* Close the connection to the database. */
    	mongo.close();
		
	}
	
    @DELETE
    @Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    public void deleteProfile(@PathParam("userId") String userId) {
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("ACCOUNTS");
    	
    	/* Delete the document containing the user's information. */
    	doc.deleteOne(new Document("_id", userId));
    	
    	/* Close the connection to the database. */
    	mongo.close();
    	
    }
}
