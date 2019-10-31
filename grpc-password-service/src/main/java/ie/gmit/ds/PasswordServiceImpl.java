package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void hash(HashRequest req, StreamObserver<HashResponse> responseObserver) {

		// Generate salt
		byte[] salt = Passwords.getNextSalt();

		// Convert password string to char array
		char[] passwordAsCharArray = req.getPassword().toCharArray();

		// Hash password
		byte[] hashedPassword = Passwords.hash(passwordAsCharArray, salt);

		// Build HashResponse
		HashResponse hashResponse = HashResponse.newBuilder()
				.setUserId(req.getUserId())
				// Copy the given bytes of the hashed password into a ByteString
				.setHashedPassword(ByteString.copyFrom(hashedPassword)) // The corresponding Java Type for the proto														// message type 'bytes' is ByteString
				// Copy the given bytes of the salt into a ByteString
				.setSalt(ByteString.copyFrom(salt)).build();

		responseObserver.onNext(hashResponse);
		responseObserver.onCompleted();

	}

//	@Override
//	public void validate(ValidationRequest req, StreamObserver<ValidationResponse> responseObserver) {
//
//		
//	}
}
