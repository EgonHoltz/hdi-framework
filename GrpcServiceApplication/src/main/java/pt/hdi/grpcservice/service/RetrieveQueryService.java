package pt.hdi.grpcservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.hdi.grpcservice.DataServiceGrpc;
import pt.hdi.grpcservice.GetDataRequest;
import pt.hdi.grpcservice.GetDataResponse;

@Service
public class RetrieveQueryService {

    private ManagedChannel channel;
    private DataServiceGrpc.DataServiceBlockingStub blockingStub;

    @Value("${gprc.querydb.service}")
    private String url;
    
    public String sendData(String collection, String fields) {
        this.channel = ManagedChannelBuilder.forTarget(url)
                        .usePlaintext()
                        .build();
        this.blockingStub = DataServiceGrpc.newBlockingStub(channel);
        GetDataRequest request = GetDataRequest.newBuilder()
                                                .setCollection(collection)
                                                .setFields(fields)
                                                .build();
        GetDataResponse response = this.blockingStub.getData(request);
        return response.getData();
    }

    // Ensure to shutdown the channel when the application is stopped or bean is destroyed
    public void shutdown() throws InterruptedException {
        channel.shutdown(); // Optionally, add a timeout
    }

}
