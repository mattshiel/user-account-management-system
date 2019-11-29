package ie.gmit.ds;

import java.util.Collection;
import java.util.HashMap;

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
		User testUser = new User(1, "user1", "user1@email.com", "password");
		usersMap.put(testUser.getUserId(), testUser);
	}

	@GET
	public Collection<User> getUsers() {
		// Return users
		return usersMap.values();
	}
}
