package LetsEatServer;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.mongodb.*;
import com.mongodb.client.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bson.*;

@Path("/users")
public class UserResources {

    @GET
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("userId") String userId) {
        ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	BasicDBObject user;
    	Document cur; 
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	user = new BasicDBObject("_id", userId);
    	cur = doc.find(user).first();
    	mongo.close();
    	if (cur == null) {
    		return "hello";
    	}
    	return cur.toJson();
    }

    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public void newUser(
    		@PathParam("userId") String userId,
    		String user) {
    	ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(user);
    	} catch (Exception e) {
    		return;
    	}
    	
    	JsonNode array = node.get("chatHist");
    	Document chat = new Document();
    	chat.append("message1", array.get("message1").textValue());
    	chat.append("message2", array.get("message2").textValue());
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("dateOfBirth", node.get("dateOfBirth").textValue())));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("name", node.get("name").textValue())));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("gender", node.get("gender").textValue())));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("matches", node.get("matches").textValue())));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("chatHist", chat)));
    	mongo.close();
    }
    
    @POST
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public void changeUser(
    		@PathParam("userId") String userId,
    		String user) {
    	ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(user);
    	} catch (Exception e) {
    		return;
    	}
    	
    	JsonNode array = node.get("chatHist");
    	Document chat = new Document();
    	chat.append("message1", array.get("message1").textValue());
    	chat.append("message2", array.get("message2").textValue());
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("dateOfBirth", node.get("dateOfBirth").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("name", node.get("name").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("gender", node.get("gender").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("matches", node.get("matches").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("chatHist", chat)));
    	mongo.close();
    }
    
    @DELETE
    @Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("userId") String userId) {
    	ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("dateOfBirth", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("name", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("gender", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("matches", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("chatHist", 1)));
    	mongo.close();    	
    	
    }
    
}
