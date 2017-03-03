package LetsEatServer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.bson.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/account_info")
public class AccountResources {
	@GET
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfile(@PathParam("userId") String userId) {	
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
    		return null;
    	}
    	return cur.toJson();
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void newProfile(
			@PathParam("userId") String userId,
			String account) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(account);
    	} catch (Exception e) {
    		return;
    	}
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	Document acc = new Document();
    	acc.put("_id", userId);
    	String hash;
    	try {
    	    hash = PassHash.createHash(node.get("password").textValue());
    	} catch (Exception e) {
    		mongo.close();
    		return;
    	}
    	acc.put("password", hash);
    	doc.insertOne(acc);
    	mongo.close();
		
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void changeProfile(
			@PathParam("userId") String userId,
			String account) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(account);
    	} catch (Exception e) {
    		return;
    	}
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("password", node.get("password").textValue())));
    	mongo.close();
		
	}
	
    @DELETE
    @Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    public void deleteProfile(@PathParam("userId") String userId) {
    	ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("password", 1)));
    	mongo.close();
    }
}
