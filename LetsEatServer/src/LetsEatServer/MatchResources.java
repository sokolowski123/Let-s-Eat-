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
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MatchResources {
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChatHistory(@PathParam("userId") String userId) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	BasicDBObject user;
    	Document cur; 
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("CHAT");
    	user = new BasicDBObject("_id", userId);
    	cur = doc.find(user).first();
    	mongo.close();
    	if (cur == null) {
    		return null;
    	}
    	return cur.toJson();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void putChatHistory(@PathParam("userId") String userId, String chat) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map;
    	JsonNode node;
    	Iterator<String> fields;
    	String field;
    	Document user;
    	
    	map = new ObjectMapper();
    	try {
    		node = map.readTree(chat);
    	} catch (Exception e) {
    		return;
    	}
    	
    	
    	fields = node.fieldNames();
    	user = new Document();
    	while (fields.hasNext()) {
    		field = fields.next();
    		user.put(field, node.get(field).intValue());
    	}
    	
    	adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("MATCH");
    	
    	Document acc = new Document();
    	acc.put(userId, user);
    	doc.insertOne(acc);
    	
    	mongo.close();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postChatHistory(@PathParam("userId") String userId, String chat) {
		ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	ObjectMapper map;
    	JsonNode node;
    	Iterator<String> fields;
    	String field;
    	Document user;
    	
    	map = new ObjectMapper();
    	try {
    		node = map.readTree(chat);
    	} catch (Exception e) {
    		return e.getMessage();
    	}
    	
    	fields = node.fieldNames();
    	user = new Document();
    	while (fields.hasNext()) {
    		field = fields.next();
    		user.put(field, node.get(field).textValue());
    	}
    	return user.toJson();
    	
    	
    	/* adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("CHAT");
    	
    	
    	
    	
    	mongo.close(); */
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
		 doc = data.getCollection("CHAT");
	    	
		 mongo.close();    	
	    	
	 }
}
