package Server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account_info")
public class AccountResources {
	@GET
	@Path("/{userId}")
	public Profile getProfile(@PathParam("userId") String userId) {
		
		/* Get account_info of user with the given userId. */
		
		return null;
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newProfile(
			@PathParam("userId") String userId,
			@FormParam("email") int age,
			@FormParam("password") int prefMaxAge) {
		
		/* Place account_info of the user with the given userId in the database. */
		
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void changeProfile(
			@PathParam("userId") String userId,
			@FormParam("email") int age,
			@FormParam("password") int prefMaxAge) {
		
		/* Change account_info of the user with the given userId to the given information. */
		
	}
	
    @DELETE
    @Path("/{userId}")
    public void deleteProfile(@PathParam("userId") String userId) {
    	
    	/* Delete the account_info of the user with the given userId. */
    	
    }
}
