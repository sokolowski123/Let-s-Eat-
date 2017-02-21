package Server;

import java.awt.Image;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/profile_info")
public class ProfileResources {

	@GET
	@Path("/{userId}")
	public Profile getProfile(@PathParam("userId") String userId) {
		
		/* Get Profile of user with the given userId. */
		
		return null;
	}
	
	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newProfile(
			@PathParam("userId") String userId,
			@FormParam("age") int age,
			@FormParam("prefMaxAge") int prefMaxAge,
			@FormParam("prefMinAge") int prefMinAge,
			@FormParam("bio") String bio,
			@FormParam("location") String location,
			@FormParam("additionalInfo") List<String> additionalInfo,
			@FormParam("profilePicture") Image profilePic) {
		
		/* Place Profile of the user with the given userId in the database. */
		
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void changeProfile(
			@PathParam("userId") String userId,
			@FormParam("age") int age,
			@FormParam("prefMaxAge") int prefMaxAge,
			@FormParam("prefMinAge") int prefMinAge,
			@FormParam("bio") String bio,
			@FormParam("location") String location,
			@FormParam("additionalInfo") List<String> additionalInfo,
			@FormParam("profilePicture") Image profilePic) {
		
		/* Change Profile of the user with the given userId to the given information. */
		
	}
	
    @DELETE
    @Path("/{userId}")
    public void deleteProfile(@PathParam("userId") String userId) {
    	
    	/* Delete the profile of the user with the given userId. */
    	
    }
}

