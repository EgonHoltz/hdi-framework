package pt.hdi.sftpservice.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import pt.hdi.sftpservice.bean.MQConnectionBean;
import pt.hdi.sftpservice.model.Configuration;
import pt.hdi.sftpservice.utils.ObjectHelper;

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

    public void sendFileToCentralizer(Configuration conf, Path filePath, boolean isFileOk){
        CachingConnectionFactory connection = null;
        System.out.println("I will open the connection with rejection centralizator");
        try {
        
            String collectionName = conf != null ? conf.getDocumentDataName().replaceAll("\\s", "").toLowerCase() : "";

            String queueName = isFileOk ? "file-accepted" : "file-rejection";

            byte[] zippedFileContent = ObjectHelper.compressFile(filePath.toString());
            if (zippedFileContent == null){
                throw new Exception("Failled to create the ZIP file");
            }
            connection = connectionFactory();
            Channel channel = connection.createConnection().createChannel(false);

            // Add a header with the queue name
            Map<String, Object> headers = new HashMap<>();
            headers.put("collection", collectionName);
            headers.put("file-name", filePath.getFileName().toString());

            BasicProperties props = new BasicProperties.Builder()
                    .headers(headers)
                    .build();

            // Publish the message
            channel.basicPublish("", queueName, props, zippedFileContent);
            System.out.println(" [X] File Sent '" + filePath + "' to queue: " + queueName +" with header: " + headers.toString());

        // Implement the ACK mechanism as needed.
        // This example does not cover consumer-side logic, including ACKs, as it focuses on message publishing.

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending msg to queue");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Error on ZIP the file");
        }
        finally {
            System.out.println("Close connection with rejection centralizator");
            connection.resetConnection();
        }
    }
	
}
