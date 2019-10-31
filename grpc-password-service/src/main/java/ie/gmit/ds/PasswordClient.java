package ie.gmit.ds;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/*  
 * Adapted basic client setup
 * Adapted from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory
*/

public class PasswordClient {

	// private member constants
	private static final Logger LOGGER = Logger.getLogger(PasswordClient.class.getName());
	private static final String LINESEPARATOR = System.lineSeparator();
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService; // Synchronous service for hashing
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService; // Asynchronous service for validation


	// Scanner
	private Scanner sc = new Scanner(System.in);

	// private member fields
	private String password;
	private ByteString hashedPassword;
	private ByteString salt;
	private int userId;

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 50558);
		try {
			// Build the HashRequest
			HashRequest req = client.buildHashRequest();

			// Send the HashRequest
			client.sendHashRequest(req);

			// Log to test client to server is working
			LOGGER.info(LINESEPARATOR + "User ID: " + client.getUserId() + LINESEPARATOR + "Password: "
					+ client.getPassword() + LINESEPARATOR + "Hashed Password: " + client.getHashedPassword()
					+ LINESEPARATOR + "Salt: " + client.getSalt());

		} finally {
			// Terminate client
			client.shutdown();
		}
	}

	public PasswordClient(String host, int port) {
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
		LOGGER.info("\nRequesting user input...");

		System.out.println("Enter ID:");
		userId = sc.nextInt();
		System.out.println("Enter Password:");
		password = sc.next();

		LOGGER.info("\nUser input received." + LINESEPARATOR);
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

			LOGGER.info("\nHash Response received." + LINESEPARATOR);

			// Store the password hash and the salt
			hashedPassword = hashResponse.getHashedPassword();
			salt = hashResponse.getSalt();
		} catch (StatusRuntimeException e) {
			LOGGER.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
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
