package ie.gmit.ds;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void hash(HashRequest req, StreamObserver<HashResponse> responseObserver) {

		int id = 5;
		HashResponse response = HashResponse.newBuilder().setUserId(id).build();
	}
}
