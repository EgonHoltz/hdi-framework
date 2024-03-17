package pt.hdi.grpcservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import pt.hdi.grpcservice.RetrieveDataRequest;
import pt.hdi.grpcservice.RetrieveDataResponse;
import pt.hdi.grpcservice.SendDataRequest;
import pt.hdi.grpcservice.SendDataResponse;
import pt.hdi.grpcservice.SendRetrieveDataServiceGrpc.SendRetrieveDataServiceImplBase;
import pt.hdi.grpcservice.model.Configuration;
import pt.hdi.grpcservice.service.ConfigurationService;
import pt.hdi.grpcservice.service.DataCentralizerService;
import pt.hdi.grpcservice.service.RetrieveQueryService;

@GrpcService
public class MessageController extends SendRetrieveDataServiceImplBase {

	@Autowired
	private ConfigurationService confService;
	
	@Autowired
	private DataCentralizerService dcService;

	@Autowired
	private RetrieveQueryService retrieveQuerySvc;
	
	@Override
	public void retrieveRqst(RetrieveDataRequest request, StreamObserver<RetrieveDataResponse> responseObserver) {
		System.out.println("called receiveRqst");
		String clientId = request.getClientId();
		String msg = request.getJsonMsg();
		
		if (clientId.isEmpty() || msg.isEmpty()) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("clientID or msg is empty").asRuntimeException());
		}
		System.out.println("clientId: " + clientId);
		System.out.println("msg: " + msg);
		
		ResponseEntity resConf = confService.getByClientId(clientId);
		if (!resConf.getStatusCode().equals(HttpStatus.OK)){
			System.out.println("Configuration not found for: " + clientId);
			RetrieveDataResponse response = RetrieveDataResponse.newBuilder().setClientId(clientId).setJsonMsg("INVALID_CLIENTID").build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		Configuration conf = (Configuration) resConf.getBody();
		if (confService.isValidMessageAndClientId(conf, clientId, msg, true)){
			String docName = conf.getDocumentDataName().replaceAll("\\s", "").toLowerCase();
			String responseQuery = retrieveQuerySvc.sendData(docName, msg);
			RetrieveDataResponse response = RetrieveDataResponse.newBuilder().setClientId(clientId).setJsonMsg(responseQuery).build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} else {
			RetrieveDataResponse response = RetrieveDataResponse.newBuilder().setClientId(clientId).setJsonMsg("INVALID_DATA").build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
	}

	@Override
	public void sendRqst(SendDataRequest request, StreamObserver<SendDataResponse> responseObserver) {
		System.out.println("called receiveRqst");
		String clientId = request.getClientId();
		String msg = request.getJsonMsg();
		
		if (clientId.isEmpty() || msg.isEmpty()) {
			responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("clientID or msg is empty").asRuntimeException());
		}
		System.out.println("clientId: " + clientId);
		System.out.println("msg: " + msg);
		
		ResponseEntity resConf = confService.getByClientId(clientId);
		if (!resConf.getStatusCode().equals(HttpStatus.OK)){
			System.out.println("Configuration not found for: " + clientId);
			SendDataResponse response = SendDataResponse.newBuilder().setClientId(clientId).setJsonMsg("INVALID_CLIENTID").build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
		Configuration conf = (Configuration) resConf.getBody();

		if (confService.isValidMessageAndClientId(conf, clientId, msg, false)){
			dcService.sendMessage(conf, msg);
			SendDataResponse response = SendDataResponse.newBuilder().setClientId(clientId).setJsonMsg("OK").build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} else {
			dcService.sendRejectedMessage(conf, msg);
			SendDataResponse response = SendDataResponse.newBuilder().setClientId(clientId).setJsonMsg("INVALID_DATA").build();
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}
	}
	
}
