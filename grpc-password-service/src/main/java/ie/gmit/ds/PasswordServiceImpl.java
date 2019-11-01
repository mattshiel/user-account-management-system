/*
 * Author: Matthew Shiel
 * Student ID: G00338622
 * Module: Distributed Systems
 * Lecturer: Dr. John French
 */

package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void hash(HashRequest hashRequest, StreamObserver<HashResponse> responseObserver) {

		// Generate salt
		byte[] salt = Passwords.getNextSalt();

		// Convert password string to char array
		char[] passwordAsCharArray = hashRequest.getPassword().toCharArray();

		// Hash password
		byte[] hashedPassword = Passwords.hash(passwordAsCharArray, salt);

		// Build HashResponse
		HashResponse hashResponse = HashResponse.newBuilder().setUserId(hashRequest.getUserId())
				// Copy the given bytes of the hashed password into a ByteString
				.setHashedPassword(ByteString.copyFrom(hashedPassword)) // The corresponding Java Type for the proto //
																		// message type 'bytes' is ByteString
				// Copy the given bytes of the salt into a ByteString
				.setSalt(ByteString.copyFrom(salt)).build();

		responseObserver.onNext(hashResponse);
		responseObserver.onCompleted();

	}

	@Override
	public void validate(ValidationRequest validationRequest, StreamObserver<BoolValue> responseObserver) {
		try {
			// isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash)
			char[] userPassword = validationRequest.getPassword().toCharArray();
			byte[] hashedPassword = validationRequest.getHashedPassword().toByteArray();
			byte[] salt = validationRequest.getSalt().toByteArray();

			// Adapted boolean if else from https://github.com/john-french/distributed-systems-labs/tree/master/grpc-async-inventory
			// Validate hashes match and return true
			if (Passwords.isExpectedPassword(userPassword, salt, hashedPassword)) {
				responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
			}
			// Validate hashes do not match and return false
			else {
				responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
			}
		}

		catch (RuntimeException ex) {
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
	}
}
