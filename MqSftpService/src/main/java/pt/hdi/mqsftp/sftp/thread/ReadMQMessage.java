package pt.hdi.mqsftp.sftp.thread;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReadMQMessage implements Runnable{

	
	public ReadMQMessage(String queueName) {
		super();
		this.qName = queueName;
	}

	private String qName;
	
	@Override
	public void run() {
		ConnectionFactory cFactory = new ConnectionFactory();
		

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
			exec.execute(new ReadMQMessage(qName));
		} catch (TimeoutException e) {
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new ReadMQMessage(qName));
		}
		
	}
	

}
