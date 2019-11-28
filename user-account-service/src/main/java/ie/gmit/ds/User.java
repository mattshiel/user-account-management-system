package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;

// Learnings based and adapted on https://github.com/john-french/artistAPI-dropwizard

public class User {

	// private member variables
	private int userId;
	private String userName;
	private String email;
	private String password;
	private ByteString hashedPassword;
	private ByteString salt;

	// default constructor needed for Jackson deserialisation
	public User() {
	}

	// Constructor for creating and update operations
	public User(int userId, String userName, String email, String password) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	// Constructor for returning data on a user/users
	public User(int userId, String userName, String email, ByteString hashedPassword, ByteString salt) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}

	/*
	 * Getters, no setters to maintain immutable nature of objects
	 */
	@JsonProperty
	public int getUserId() {
		return userId;
	}

	@JsonProperty
	public String getUserName() {
		return userName;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public ByteString getHashedPassword() {
		return hashedPassword;
	}

	@JsonProperty
	public ByteString getSalt() {
		return salt;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", hashedPassword=" + hashedPassword + ", salt=" + salt + "]";
	}
}
