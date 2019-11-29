/*
 * Author: Matthew Shiel
 * Student ID: G00338622
 * Module: Distributed Systems
 * Lecturer: Dr. John French
 */

package ie.gmit.ds.client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.ds.ValidationRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

/*  
 * Adapted basic client setup
 * Adapted from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory
*/

public class UserClient {

	// private member constants
	private static final Logger LOGGER = Logger.getLogger(UserClient.class.getName());
	private static final String LINESEPARATOR = System.lineSeparator();
	
	private static final String HOST = "localhost";
	private static final int PORT = 50552;
	
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService; // Synchronous service for hashing																				
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService; // Asynchronous service for validation

	// private member fields
	private String password;
	private ByteString hashedPassword;
	private ByteString salt;
	private int userId;
	private Scanner sc = new Scanner(System.in);

	public UserClient(String host, int port) {
		// Create channel with no SSL for testing purposes
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		// Create stubs
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		// Terminate client channel
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void getUserInput() {
		System.out.println("Please enter your user ID:");
		userId = sc.nextInt();
		System.out.println("Please enter your password:");
		password = sc.next();
	}

	public HashRequest buildHashRequest() {
		getUserInput();

		LOGGER.info("\nBuilding Hash Request...");

		// Build HashRequest
		HashRequest hashRequest = HashRequest.newBuilder().setUserId(userId).setPassword(password).build();

		LOGGER.info("\nHash Request built" + LINESEPARATOR);

		return hashRequest;
	}

	public void sendHashRequest(HashRequest req) {

		HashResponse hashResponse;

		try {
			LOGGER.info("\nSending Hash Request...");

			// Build HashResponse
			hashResponse = syncPasswordService.hash(req);

			LOGGER.info("\nPassword hashed successfully! HashResponse received." + LINESEPARATOR);

			// Store the password hash and the salt
			setHashedPassword(hashResponse.getHashedPassword());
			salt = hashResponse.getSalt();
		} catch (StatusRuntimeException e) {
			LOGGER.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return;
		}
	}

	public void sendValidationRequest() {
		// Create stream observer to watch for validation confirmation
		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {

			// Return message if login was successful or unsuccessful 
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					System.out.println("Login Successful! Password Validated.");
				} else {
					System.out.println("Username or Password is incorrect");
				}
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("An Error has occurred.. " + t.getLocalizedMessage());
			}

			@Override
			public void onCompleted() {
				System.exit(0);
			}
		};

		try {
			getUserInput();
			// Validate password
			asyncPasswordService.validate(ValidationRequest.newBuilder().setPassword(password)
					.setHashedPassword(hashedPassword).setSalt(salt).build(), responseObserver);
		} catch (StatusRuntimeException ex) {
			LOGGER.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
			return;
		}
	}

	/*
	 * Getters and Setters
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ByteString getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(ByteString hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public ByteString getSalt() {
		return salt;
	}

	public void setSalt(ByteString salt) {
		this.salt = salt;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}