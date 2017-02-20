package Server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class ServerResources {
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserName() {
		return "Name Placeholder"; 
	}
	
	
	
}
