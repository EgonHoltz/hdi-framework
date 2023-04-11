package pt.hdi.grpcservice.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import pt.hdi.grpcservice.ServiceDataRequest;
import pt.hdi.grpcservice.ServiceDataResponse;
import pt.hdi.grpcservice.dataGrpc.dataImplBase;

@GrpcService
public class MessageService extends dataImplBase {

	@Override
	public void sendRqst(ServiceDataRequest request, StreamObserver<ServiceDataResponse> responseObserver) {
		String clientId = request.getClientId();
		String msg = request.getJsonMsg();
		
		if (clientId.isEmpty() || msg.isEmpty()) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("clientID or msg is empty").asRuntimeException());
		}
		
		ServiceDataResponse response = ServiceDataResponse.newBuilder().setClientId(clientId).setJsonMsg("OK").build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		
	}
	
}
