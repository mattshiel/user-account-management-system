package ie.gmit.ds;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void hash(HashRequest req, StreamObserver<HashResponse> responseObserver) {

		HashResponse response = HashResponse.newBuilder().setUserId(req.getUserId()).setHashedPassword(req.getPassword()).build();
		responseObserver.onNext(response);
        responseObserver.onCompleted();
	}
}
