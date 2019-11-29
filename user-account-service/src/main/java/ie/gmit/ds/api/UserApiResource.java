package ie.gmit.ds.api;

import java.util.Collection;
import java.util.HashMap;
import ie.gmit.ds.database.UserDB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Handle requests to the users base path and return JSON responses
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserApiResource {
	
	private HashMap<Integer, User> usersMap = new HashMap<Integer, User>();

	public UserApiResource() {
		
	}

	@GET
	public Collection<User> getUsers() {
		// Return users
		return UserDB.getUsers();
	}
}
