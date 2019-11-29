package ie.gmit.ds.resources;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ie.gmit.ds.api.User;
import ie.gmit.ds.client.UserClient;
import ie.gmit.ds.database.UserDB;

/*
 * Class adapted, modified and based on https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/
 */

// Handle requests to the users base path and return JSON responses
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserApiResource {

	private final Validator validator;
	private UserClient userClient;
	
	// CLIENT HOST AND PORT LOCATION
//	 private final String HOST = "localhost";
//	 private final int PORT = 50551;

	// Default constructor used to initialize validator
	public UserApiResource(Validator validator) {
		this.validator = validator;
		// Client launch would go here
		//this.userClient = new UserClient(HOST, PORT);
	}

	// Retrieve a user from the database
	@GET
	@Path("/{userId}")
	public Response getById(@PathParam("userId") int id) {
		User user = UserDB.get(id);
		if (user != null) {
			return Response.status(Status.OK).entity("User Retrieved!").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("Error, User Not Found!").build();
		}
	}

	// Create a user and add it to the database
	@POST
	public Response create(User user) throws URISyntaxException {
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		User u = UserDB.get(user.getUserId());
		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<>();
			for (ConstraintViolation<User> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString());
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build();
		}
		// Create new user if it doesn't exist
		if (u == null) {
			UserDB.update(user.getUserId(), user);
			return Response.status(Status.OK).entity("User Created!").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("User Already Exists. Please try again.").build();
		}
	}

	// Update user
	@PUT
	@Path("/{userId}")
	public Response updateById(@PathParam("userId") int id, User user) {
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		User u = UserDB.get(user.getUserId());
		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<>();
			for (ConstraintViolation<User> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString());
			}
			return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
		}
		if (u != null) {
			// Update user
			UserDB.update(id, user);
			return Response.status(Status.OK).entity("User Updated!").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("Error, User Not Found!").build();
		}
	}

	// Remove a user from the database
	@DELETE
	@Path("/{userId}")
	public Response removeById(@PathParam("userId") int id) {
		User user = UserDB.get(id);
		if (user != null) {
			UserDB.remove(id);
			return Response.status(Status.OK).entity("User Deleted!").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("Error, User Not Found!").build();
		}
	}

	/*
	 * Returns a list of all users in the database
	 */
	@GET
	public Response getUsers() {
		// Return users
		return Response.ok(UserDB.getUsers()).build();
	}

}
