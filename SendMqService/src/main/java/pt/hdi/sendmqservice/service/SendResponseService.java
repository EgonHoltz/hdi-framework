package pt.hdi.sendmqservice.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.hdi.sendmqservice.DataServiceGrpc;
import pt.hdi.sendmqservice.GetDataRequest;
import pt.hdi.sendmqservice.GetDataResponse;
import pt.hdi.sendmqservice.config.GrpcConnectionConfig;

@Service
public class SendResponseService {

    private ManagedChannel channel;
    private DataServiceGrpc.DataServiceBlockingStub blockingStub;

    @Autowired
	private ApplicationContext ctx;

    @PostConstruct
    private void init(){
        Environment env = ctx.getEnvironment();

        String url = env.getProperty("gprc.querydb.service");
        this.channel = ManagedChannelBuilder.forTarget(url)
                        .usePlaintext()
                        .build();
        this.blockingStub = DataServiceGrpc.newBlockingStub(channel);
    }
    
    public String sendData(String collection, String fields) {
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
