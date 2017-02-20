package Server;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResources {

    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_XML)
    public User getUser(@PathParam("userId") String userId) {
   
    	/*Get Users from database.*/
    	
    	return null;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void newUser(
    		@FormParam("dataOfBirth") int dataOfBirth,
    		@FormParam("userId") String userId, 
    		@FormParam("name") String name,
    		@FormParam("gender") String gender,
    		@FormParam("matchs") List<List <Match>> matches) {
    	User user = new User(dataOfBirth, userId, name, gender, matches);
    	
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
