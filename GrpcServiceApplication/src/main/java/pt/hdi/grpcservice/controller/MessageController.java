package pt.hdi.grpcservice.controller;

import org.springframework.beans.factory.annotation.Autowired;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import pt.hdi.grpcservice.ServiceDataRequest;
import pt.hdi.grpcservice.ServiceDataResponse;
import pt.hdi.grpcservice.dataGrpc.dataImplBase;
import pt.hdi.grpcservice.model.Configuration;
import pt.hdi.grpcservice.service.ConfigurationService;

@GrpcService
public class MessageController extends dataImplBase {

	@Autowired
	private ConfigurationService confService;
	
	@Override
	public void sendRqst(ServiceDataRequest request, StreamObserver<ServiceDataResponse> responseObserver) {
		String clientId = request.getClientId();
		String msg = request.getJsonMsg();
		
		if (clientId.isEmpty() || msg.isEmpty()) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("clientID or msg is empty").asRuntimeException());
		}
		
		Configuration clientDoc = confService.getByDocumentName(clientId);
		
		if(clientDoc == null) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("client not found").asRuntimeException());			
		}
		
		ServiceDataResponse response = ServiceDataResponse.newBuilder().setClientId(clientId).setJsonMsg("OK").build();
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		
	}
	
}
