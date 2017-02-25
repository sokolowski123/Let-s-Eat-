package LetsEatServer;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.*;

@Path("/users")
public class UserResources {

    @GET
    @Path("{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUser(@PathParam("userId") String userId) {
        ServerAddress adr;
    	MongoClient mongo;
    	MongoDatabase data;
    	MongoCollection<Document> doc;
    	BasicDBObject user;
    	Document cur;
    	
    	adr = new ServerAddress("35.162.117.161");
    	mongo = new MongoClient(adr);
    	data = mongo.getDatabase("Users");
    	doc = data.getCollection("USERS");
    	user = new BasicDBObject("_id", userId);
    	cur = doc.find(user).first();
    	mongo.close();
    	
    	return cur.get("_id").toString();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void newUser(
    		@FormParam("dataOfBirth") int dataOfBirth,
    		@FormParam("userId") String userId, 
    		@FormParam("name") String name,
    		@FormParam("gender") String gender,
    		@FormParam("matchs") List<List <Match>> matches) {
    	
    	/* Put user in the database. */
    	
    }
    
    @POST
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void changeUser(
    		@FormParam("dataOfBirth") int dataOfBirth,
    		@FormParam("userId") String userId, 
    		@FormParam("name") String name,
    		@FormParam("gender") String gender,
    		@FormParam("matchs") List<List <Match>> matches) {
    	
    	/* Change user information in the database. */
    	
    }
    
    @DELETE
    @Path("{userId}")
    public void deleteUser(@PathParam("userId") String userId) {
    	
    	/* Delete user from database. */
    	
    }
    
}
