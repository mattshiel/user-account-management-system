package ie.gmit.ds;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

/*  
 * Adapted basic client setup
 * Adapted from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory
*/

public class PasswordClient {

	// private member constants
	private static final Logger LOGGER = Logger.getLogger(PasswordClient.class.getName());
	private static final String LINESEPARATOR = System.lineSeparator();
	private static final String HOST = "localhost";
	private static final int PORT = 50568;
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService; // Synchronous service for
																						// hashing
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService; // Asynchronous service for validation

	// Scanner
	private Scanner sc = new Scanner(System.in);

	// private member fields
	private String password;
	private ByteString hashedPassword;
	private ByteString salt;
	private int userId;

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient(HOST, PORT);
		try {
			LOGGER.info("\nRequesting user input for hashing...");
			
			// Build a HashRequest
			HashRequest req = client.buildHashRequest();
			
			LOGGER.info(LINESEPARATOR + "User input received." + LINESEPARATOR);

			// Send the HashRequest
			client.sendHashRequest(req);
			
			LOGGER.info(LINESEPARATOR + "Requesting user input for validation...");
			
			// Send a ValidationRequest
            client.sendValidationRequest();
            
			LOGGER.info(LINESEPARATOR + "User input received.");

			// Log to test client to server is working
			LOGGER.info(LINESEPARATOR + "User ID: " + client.getUserId() + LINESEPARATOR + "Password: "
					+ client.getPassword() + LINESEPARATOR + "Hashed Password: " + client.getHashedPassword()
					+ LINESEPARATOR + "Salt: " + client.getSalt());
			

		} finally {
            // Keep process alive to receive async response
            Thread.currentThread().join();
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
		System.out.println("Enter ID:");
		userId = sc.nextInt();
		System.out.println("Enter Password:");
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
			
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					System.out.println("Login Successful! Password Validated!");
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
