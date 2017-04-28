package LetsEatServer;

import java.io.IOException;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("/meeting/user/{userId}")
public class MeetingResources {
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMeetings(@PathParam("userId") String userId) {
		
		/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, return an error. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: User could not be found.");
    		return message.toJson();
    	}
    	
    	/* Close connection to MongoDb. */
		mongo.close();
		
		/* Return the meeting information. */
		return user.toJson();
		
	}
	
	@GET
	@Path("/match/{matchId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMeeting(@PathParam("userId") String userId, @PathParam("matchId") String matchId) {
		
		/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, return an error. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: User could not be found.");
    		return message.toJson();
    	}
    	
    	/* Replace the '.' character with a '_' character for use as a key in MongoDb. */
    	String fixedMatchId = matchId.replace('.', '_');
    	
    	/* Get the information pertaining to the meeting with the given match. If their is no meeting set, return an error. */
    	Document meeting = (Document) user.get(fixedMatchId);
    	if (meeting == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: No meeting with the given user exists.");
    		return message.toJson();
    	}
    	
    	/* Close connection to MongoDb. */
		mongo.close();
		
		/* Return the meeting information. */
		return meeting.toJson();
		
	}
	
	@PUT
	@Path("/match/{matchId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String putMeeting(@PathParam("userId") String userId, @PathParam("matchId") String matchId, String meeting) {
		
		/* Create a JsonNode from the JSON object given by the client. The JSON object given by the client is stored in meeting. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
			node = map.readTree(meeting);
		} catch (JsonProcessingException e) {
			Document message = new Document();
    		message.put("message", "Error: JsonProcessingException\nError Message: " + e.getMessage());
    		return message.toJson();
		} catch (IOException e) {
			Document message = new Document();
    		message.put("message", "Error: IOException\nError Message: " + e.getMessage());
    		return message.toJson();
		}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, return an error. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: User could not be found.");
    		return message.toJson();
    	}
    	
    	/* Replace the '.' character with a '_' character for use as a key in MongoDb. */
    	String fixedMatchId = matchId.replace('.', '_');
    	
    	/* Get the information pertaining to the meeting with the given match. If their is no meeting set, return an error. */
    	Document meetingInfo = (Document) user.get(fixedMatchId);
    	if (meetingInfo == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: No meeting with the given user exists.");
    		return message.toJson();
    	}
    	
    	/* Update the user's email/id if given. */
    	JsonNode id = node.get("_id");
    	if (id != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", id.textValue())));
    	}
    	
    	/* Update restaurant information if it was given by the client. */
    	JsonNode restaurant = node.get("restaurant");
    	if (restaurant != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("restaurant", restaurant.textValue());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Update time information if it was given by the client. */
    	JsonNode time = node.get("time");
    	if (time != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("time", time.textValue());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Update isGood information if it was given by the client. */
    	JsonNode isGood = node.get("isGood");
    	if (isGood != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("isGood", isGood.asInt());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Close connection to the database. */
    	mongo.close();
    	
    	Document message = new Document();
		message.put("message", "Success");
		return message.toJson();
    	
	}
	
	@POST
	@Path("/match/{matchId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postMeeting(@PathParam("userId") String userId, @PathParam("matchId") String matchId, String meeting)  {
		
		/* Create a JsonNode from the JSON object given by the client. The JSON object given by the client is stored in meeting. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
			node = map.readTree(meeting);
		} catch (JsonProcessingException e) {
			Document message = new Document();
    		message.put("message", "Error: JsonProcessingException\nError Message: " + e.getMessage());
    		return message.toJson();
		} catch (IOException e) {
			Document message = new Document();
    		message.put("message", "Error: IOException\nError Message: " + e.getMessage());
    		return message.toJson();
		}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, create a new document with the given userId. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		user = new Document();
    		user.put("_id", userId);
    		doc.insertOne(user);
    	}
    	user = doc.find(new Document("_id", userId)).first();
    	
    	/* Replace the '.' character with a '_' character for use as a key in MongoDb. */
    	String fixedMatchId = matchId.replace('.', '_');
    	
    	/* Get the information pertaining to the meeting with the given match. If their is no meeting set, create a new meeting with the given match as the key. */
    	Document meetingInfo = (Document) user.get(fixedMatchId);
    	if (meetingInfo == null) {
    		meetingInfo = new Document();
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Update the user's email/id if given. */
    	JsonNode id = node.get("_id");
    	if (id != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", id.textValue())));
    	}
    	
    	user = doc.find(new Document("_id", userId)).first();
    	
    	/* Update restaurant information if it was given by the client. */
    	JsonNode restaurant = node.get("restaurant");
    	if (restaurant != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("restaurant", restaurant.textValue());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Update time information if it was given by the client. */
    	JsonNode time = node.get("time");
    	if (time != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("time", time.textValue());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Update isGood information if it was given by the client. */
    	JsonNode isGood = node.get("isGood");
    	if (isGood != null) {
    		meetingInfo = (Document) user.get(fixedMatchId);
    		meetingInfo.put("isGood", isGood.asInt());
    		doc.updateOne(new Document("_id", userId), new Document("$set", new Document(fixedMatchId, meetingInfo)));
    	}
    	
    	/* Close connection to the database. */
    	mongo.close();
    	
    	Document message = new Document();
		message.put("message", "Success");
		return message.toJson();
    	
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMeetings(@PathParam("userId") String userId) {
		
		/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, return an error. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: User could not be found.");
    		return message.toJson();
    	}
    	
    	doc.deleteOne(new Document("_id", userId));
    	
    	/* Close connection to MongoDb. */
		mongo.close();
		
		Document message = new Document();
		message.put("message", "Success");
		return message.toJson();
	}
	
	@DELETE
	@Path("/match/{matchId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMeeting(@PathParam("userId") String userId, @PathParam("matchId") String matchId) {
		
		/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("MEETINGS");
    	
    	/* If the user does not exist, return an error. */
    	Document user = doc.find(new Document("_id", userId)).first();
    	if (user == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: User could not be found.");
    		return message.toJson();
    	}
    	
    	/* Replace the '.' character with a '_' character for use as a key in MongoDb. */
    	String fixedMatchId = matchId.replace('.', '_');
    	
    	/* Get the information pertaining to the meeting with the given match. If their is no meeting set, return an error. */
    	Document meeting = (Document) user.get(fixedMatchId);
    	if (meeting == null) {
    		mongo.close();
    		Document message = new Document();
    		message.put("message", "Error: No meeting with the given user exists.");
    		return message.toJson();
    	}
    	
    	doc.updateOne(new Document("_id", userId), new Document("$unset", new Document(fixedMatchId, meeting)));
    	
    	/* Close connection to MongoDb. */
		mongo.close();
		
		Document message = new Document();
		message.put("message", "Success");
		return message.toJson();
	}

}
