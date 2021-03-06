/*
 * Author: Matthew Shiel
 * Student ID: G00338622
 * Module: Distributed Systems
 * Lecturer: Dr. John French
 */

package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PasswordServer {

	private Server grpcServer;
	private static final Logger logger = Logger.getLogger(PasswordServer.class.getName());
	private static final int PORT = 50550;

	public static void main(String[] args) throws IOException, InterruptedException {
		// Create server instance
		final PasswordServer passwordServer = new PasswordServer();
		passwordServer.start();
		passwordServer.blockUntilShutdown();
	}

	// Start the server
	private void start() throws IOException {
		grpcServer = ServerBuilder.forPort(PORT).addService(new PasswordServiceImpl()).build().start();
		logger.info("Server started, listening on " + PORT);

	}

	// Stop the server
	private void stop() {
		if (grpcServer != null) {
			grpcServer.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (grpcServer != null) {
			grpcServer.awaitTermination();
		}
	}
}
