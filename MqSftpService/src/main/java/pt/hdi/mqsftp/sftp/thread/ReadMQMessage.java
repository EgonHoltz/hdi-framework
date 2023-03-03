package pt.hdi.mqsftp.sftp.thread;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.config.MessageListener;

public class ReadMQMessage implements Runnable{

	
	public ReadMQMessage(String queueName, MQConnectionBean conConfig) {
		super();
		this.qName = queueName;
		this.conConfig = conConfig;
	}

	private String qName;
	private MQConnectionBean conConfig;
	
    @Bean
    public CachingConnectionFactory connectionFactory() {
    	CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setHost(conConfig.getHost());
        ccf.setUsername(conConfig.getUsername());
        ccf.setPassword(conConfig.getPassword());
        return ccf;
    }
    	
	@Override
	public void run() {
		try{
			SimpleMessageListenerContainer smlc = new SimpleMessageListenerContainer(connectionFactory());
			smlc.setConnectionFactory(connectionFactory());
			smlc.setQueueNames(qName);
			smlc.setMessageListener(new MessageListenerAdapter(new MessageListener()));
			smlc.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			//exec.execute(new ReadMQMessage(qName));
		}
		
	}
	

}
