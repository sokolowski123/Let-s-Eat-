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

@Path("/profile_info")
public class ProfileResources {

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
			String profile) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(profile);
    	} catch (Exception e) {
    		return;
    	}
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("age", node.get("age").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("prefMaxAge", node.get("prefMaxAge").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("prefMinAge", node.get("prefMinAge").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("bio", node.get("bio").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("location", node.get("location").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("additionalInfo", node.get("additionalInfo").textValue())));
    	mongo.close();
		
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void changeProfile(
			@PathParam("userId") String userId,
			String profile) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(profile);
    	} catch (Exception e) {
    		return;
    	}
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("age", node.get("age").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("prefMaxAge", node.get("prefMaxAge").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("prefMinAge", node.get("prefMinAge").intValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("bio", node.get("bio").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("location", node.get("location").textValue())));
    	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("additionalInfo", node.get("additionalInfo").textValue())));
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
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("age", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("prefMaxAge", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("prefMinAge", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("bio", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("location", 1)));
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document("additionalInfo", 1)));
    	mongo.close();
    	
    }
}

