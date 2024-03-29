package pt.hdi.mqservice.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Connection;

import pt.hdi.mqservice.bean.MQConnectionBean;
import pt.hdi.mqservice.model.Configuration;

@Service
public class DataCentralizerService {

    @Autowired
	private ApplicationContext ctx;

    public CachingConnectionFactory connectionFactory() {
    	Environment env = ctx.getEnvironment();
    	MQConnectionBean conConfig = new MQConnectionBean(env);
    	CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setHost(conConfig.getHost());
        ccf.setUsername(conConfig.getUsername());
        ccf.setPassword(conConfig.getPassword());
        return ccf;
    }

    public void sendMessage(Configuration conf, String message) {
		CachingConnectionFactory connection = null;
        System.out.println("I will open the connection with centralizator");
        try {
            String collectionName = conf.getDocumentDataName().replaceAll("\\s", "").toLowerCase();
			String queueName = "centralizator";
			connection = connectionFactory();
			Channel channel = connection.createConnection().createChannel(false);

            // Add a header with the queue name
            Map<String, Object> headers = new HashMap<>();
            headers.put("queue-name", queueName);
            headers.put("collection", collectionName);

            BasicProperties props = new BasicProperties.Builder()
                    .headers(headers)
                    .build();

            // Publish the message
            channel.basicPublish("", queueName, props, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "' to queue: " + queueName +" with header: " + headers.toString());

            // Implement the ACK mechanism as needed.
            // This example does not cover consumer-side logic, including ACKs, as it focuses on message publishing.

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending msg to queue");
        }
		 finally {
            System.out.println("Close connection with centralizator");
			connection.resetConnection();
		 }
    }

    public void sendRejectedMessage(Configuration conf, String message){
        CachingConnectionFactory connection = null;
        System.out.println("I will open the connection with rejection centralizator");
        try {
            String collectionName = conf.getDocumentDataName().replaceAll("\\s", "").toLowerCase();
            String queueName = "rejection";
            connection = connectionFactory();
            Channel channel = connection.createConnection().createChannel(false);

            // Add a header with the queue name
            Map<String, Object> headers = new HashMap<>();
            headers.put("collection", collectionName);
            headers.put("reason", "INVALID");

            BasicProperties props = new BasicProperties.Builder()
                    .headers(headers)
                    .build();

            // Publish the message
            channel.basicPublish("", queueName, props, message.getBytes("UTF-8"));
            System.out.println(" [xXx] Rejection Sent '" + message + "' to queue: " + queueName +" with header: " + headers.toString());

        // Implement the ACK mechanism as needed.
        // This example does not cover consumer-side logic, including ACKs, as it focuses on message publishing.

    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Error sending msg to queue");
    }
     finally {
        System.out.println("Close connection with rejection centralizator");
        connection.resetConnection();
     }
    }

	
}
