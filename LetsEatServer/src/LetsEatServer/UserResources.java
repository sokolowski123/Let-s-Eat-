package LetsEatServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("USERS");
    	
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
    public void changeUser(@PathParam("userId") String userId, String user) {
    	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(user);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("USERS");
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	     
    	/* Update the user's age if given. */
    	if (node.get("age") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("age", node.get("age").asInt())));
    	}
    	
    	/* Update the user's maxRange if given. */
    	if (node.get("maxRange") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("maxRange", node.get("maxRange").asInt())));
    	}
    	
    	/* Update the user's location if given. */
    	if (node.get("location") != null) {
    	    JsonNode array = node.get("location");
            Iterator<JsonNode> arrayValues = array.elements();
            List<Double> values = new ArrayList<Double>();
            while (arrayValues.hasNext()) {
         	    JsonNode value = arrayValues.next();
        	    values.add(Double.valueOf(value.asDouble()));
            }
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("location", values)));
    	}
         
    	/* Update the user's biography if given. */
        if (node.get("bio") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("bio", node.get("bio").textValue())));
        }
    	    
        /* Update the user's gender if given. */
        if (node.get("gender") != null) {
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("gender", node.get("gender").textValue())));
        }
         
        /* Update the user's name if given. */
        if (node.get("name") != null) {
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("name", node.get("name").textValue())));
        } 
        
        if (node.get("chatNotification") != null) {
        	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("chatNotification", node.get("chatNotification").asInt())));
        }
        
        if (node.get("meetingNotification") != null) {
        	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("meetingNotification", node.get("meetingNotification").asInt())));
        }
        
        if (node.get("matchNotification") != null) {
        	doc.updateOne(new Document("_id", userId), new Document("$set", new Document("matchNotification", node.get("matchNotification").asInt())));
        }
        
        /* Close connection to database. */
        mongo.close();
    
    }
    
    @POST
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public void createUser(@PathParam("userId") String userId, String user) {
    	
    	/* Create a JsonNode from the JSON object given by the user. The JSON object given by the user is stored in user. */
    	ObjectMapper map = new ObjectMapper();
    	JsonNode node;
    	try {
    		node = map.readTree(user);
    	} catch (Exception e) {
    		return;
    	}
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("USERS");
    	
    	/* Create a new document in the collection if one has not been created yet. */
    	if (doc.find(new Document("_id", userId)).first() == null) {
    		 doc.insertOne(new Document("_id", userId));
    	}
    	
    	/* Update the user's email/id if given. */
    	if (node.get("_id") != null) {
    	     doc.updateOne(new Document("_id", userId), new Document("$set", new Document("_id", node.get("_id").textValue())));
    	}
    	     
    	/* Update the user's age if given. */
    	if (node.get("age") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("age", node.get("age").asInt())));
    	}
    	
    	/* Update the user's maxRange if given. */
    	if (node.get("maxRange") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("maxRange", node.get("maxRange").asInt())));
    	}
    	
    	/* Update the user's location if given. */
    	if (node.get("location") != null) {
    	    JsonNode array = node.get("location");
            Iterator<JsonNode> arrayValues = array.elements();
            List<Double> values = new ArrayList<Double>();
            while (arrayValues.hasNext()) {
         	    JsonNode value = arrayValues.next();
        	    values.add(Double.valueOf(value.asDouble()));
            }
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("location", values)));
    	}
         
    	/* Update the user's biography if given. */
        if (node.get("bio") != null) {
    	    doc.updateOne(new Document("_id", userId), new Document("$set", new Document("bio", node.get("bio").textValue())));
        }
    	    
        /* Update the user's gender if given. */
        if (node.get("gender") != null) {
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("gender", node.get("gender").textValue())));
        }
         
        /* Update the user's name if given. */
        if (node.get("name") != null) {
            doc.updateOne(new Document("_id", userId), new Document("$set", new Document("name", node.get("name").textValue())));
        } 
        
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("chatNotification", 0)));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("meetingNotification", 0)));
        doc.updateOne(new Document("_id", userId), new Document("$set", new Document("matchNotification", 0)));
        
        /* Close connection to database. */
        mongo.close();
        
    }
    
    @DELETE
    @Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
    public void deleteUser(@PathParam("userId") String userId) {
    	
    	/* Connect to the MongoDB database. */
    	ServerAddress adr = new ServerAddress("ec2-52-41-45-85.us-west-2.compute.amazonaws.com", 27017);
    	MongoClient mongo = new MongoClient(adr);
    	MongoDatabase data = mongo.getDatabase("Users");
    	MongoCollection<Document> doc = data.getCollection("USERS");

    	/* Delete the document containing the user's information. */
    	doc.deleteOne(new Document("_id", userId));

        /* Close connection to database. */
        mongo.close();
    	
    }
    
}
