package pt.hdi.mqsftp.sftp.thread;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;

public class ReadMQMessage implements Runnable{

	
	public ReadMQMessage(String queueName, MQConnectionBean conConfig) {
		super();
		this.qName = queueName;
		this.conConfig = conConfig;
	}

	private String qName;
	private MQConnectionBean conConfig;
	
	@Override
	public void run() {
		ConnectionFactory cFactory = new ConnectionFactory();
		cFactory.setHost(conConfig.getHost());
		cFactory.setUsername(conConfig.getUsername());
		cFactory.setPassword(conConfig.getPassword());
		cFactory.setPort(conConfig.getPort());
		

		try(Connection con = cFactory.newConnection();
				Channel chnl = con.createChannel()){
			chnl.queueDeclare(qName,false,false,false,null);
			
			DeliverCallback dcb = (consumerTag, delivery) -> {
				String msg = new String(delivery.getBody().toString());
				System.out.println("Received msg -> " + msg);
			};
			chnl.basicConsume(qName, true, dcb, consumerTag -> {});
			
		} catch (IOException e) {
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			//exec.execute(new ReadMQMessage(qName));
		} catch (TimeoutException e) {
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			//exec.execute(new ReadMQMessage(qName));
		}
		
	}
	

}
