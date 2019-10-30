package ie.gmit.ds;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

/*  
	Adapted from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory
*/

public class PasswordClient {

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	public PasswordClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 50552);
		try {
			client.hash(12, "password");
		} finally {
			// TODO Auto-generated catch block
			client.shutdown();
		}
	}

	public void hash(int userId, String password) throws IOException {
		HashRequest.Builder builder = HashRequest.newBuilder();
		builder.setUserId(userId).setPassword(password);
		HashRequest request = builder.build();
		
		HashResponse response;
		try {
			response = syncPasswordService.hash(request);
		} catch (StatusRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		logger.info("Greetings " + response.getUserId() + " Your Password is " + response.getHashedPassword());
	}

}
