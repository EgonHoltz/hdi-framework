package pt.hdi.mqsftp.sftp.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import pt.hdi.mqsftp.sftp.config.MQConnectionBean;

public class MQWriterService {

	public void sendMQMessageByQueue(String qName, MQConnectionBean mqConConfig, String msg) {
		
		if(msg.isBlank()) {
			msg = "{ClientId:'Client1',ClientFName:'FirstName',ClientLName:'LastName',Docs:[DocType:NIF,DocNumber:'21651654']}";
		}
		
		ConnectionFactory cnFactory = new ConnectionFactory();
		cnFactory.setHost(mqConConfig.getHost());
		cnFactory.setUsername(mqConConfig.getUsername());
		cnFactory.setPassword(mqConConfig.getPassword());
		
		try(Connection cn = cnFactory.newConnection();
				Channel chnl = cn.createChannel()){
			
			
			chnl.queueDeclare(qName,false,false,false,null);
			chnl.basicPublish("", qName, null, msg.getBytes());
			System.out.println("Message sent: " + msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
