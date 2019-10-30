package ie.gmit.ds;

import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	@Override
	public void hash(HashRequest req, StreamObserver<HashResponse> responseObserver) {
			
	}
	
//	@Override
//	public void validate(ValidationRequest req, StreamObserver<ValidationResponse> responseObserver) {
//
//		
//	}
}
