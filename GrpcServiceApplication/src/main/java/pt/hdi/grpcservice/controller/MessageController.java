package pt.hdi.grpcservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import pt.hdi.grpcservice.ServiceDataRequest;
import pt.hdi.grpcservice.ServiceDataResponse;
import pt.hdi.grpcservice.ReceiveDataServiceGrpc.ReceiveDataServiceImplBase;
import pt.hdi.grpcservice.model.Configuration;
import pt.hdi.grpcservice.service.ConfigurationService;
import pt.hdi.grpcservice.service.DataCentralizerService;

@GrpcService
public class MessageController extends ReceiveDataServiceImplBase {

	@Autowired
	private ConfigurationService confService;
	
	 @Autowired
	 private DataCentralizerService dcService;
	
	@Override
	public void receiveRqst(ServiceDataRequest request, StreamObserver<ServiceDataResponse> responseObserver) {
		System.out.println("called receiveRqst");
		String clientId = request.getClientId();
		String msg = request.getJsonMsg();
		
		if (clientId.isEmpty() || msg.isEmpty()) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("clientID or msg is empty").asRuntimeException());
		}
		System.out.println("clientId: " + clientId);
		System.out.println("msg: " + msg);
		
		//Configuration clientDoc = confService.getByDocumentName(clientId);
		
		// if(clientDoc == null) {
		// 	responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("client not found").asRuntimeException());			
		// }
	
		// ResponseEntity<String> res = dcService.sendMessageToCentralizedServcer(clientDoc, msg);
		
		ServiceDataResponse response = ServiceDataResponse.newBuilder().setClientId(clientId).setJsonMsg("OK").build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();			
		// if (HttpStatus.ACCEPTED.equals(res.getStatusCode())) {
		// } else {
		// 	responseObserver.onError(Status.ABORTED.withDescription(res.getBody() + " while tries to send msg to centralizator").asRuntimeException());						
		// }
	}
	
}
