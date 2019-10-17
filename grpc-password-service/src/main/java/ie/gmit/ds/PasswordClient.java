package ie.gmit.ds;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class PasswordClient {

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncInventoryService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncInventoryService;

	public PasswordClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncInventoryService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncInventoryService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 50550);
		try {
			client.greet(12, "password");
		} finally {
			// TODO Auto-generated catch block
			client.shutdown();
		}
	}

	public void greet(int userId, String password) throws IOException {
		HashRequest.Builder builder = HashRequest.newBuilder();
		builder.setUserId(userId).setPassword(password);
		HashRequest request = builder.build();
		
		HashResponse response;
		try {
			response = syncInventoryService.hash(request);
		} catch (StatusRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		logger.info("Greetings " + response.getUserId() + " Your Password is " + response.getHashedPassword());
	}

}
