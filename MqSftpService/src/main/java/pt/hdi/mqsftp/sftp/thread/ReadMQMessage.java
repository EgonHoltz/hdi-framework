package pt.hdi.mqsftp.sftp.thread;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.config.MessageListener;
import pt.hdi.mqsftp.sftp.service.ConfigurationService;

@Component
public class ReadMQMessage implements Runnable{

	private ApplicationContext ctx;
	
	
	public ReadMQMessage() {
		super();
	}

	public ReadMQMessage(String queueName, ApplicationContext ctx) {
		super();
		this.qName = queueName;
		this.ctx = ctx;
	}

	private String qName;
	
    public CachingConnectionFactory connectionFactory() {
    	Environment env = ctx.getEnvironment();
    	MQConnectionBean conConfig = new MQConnectionBean(env);
    	CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setHost(conConfig.getHost());
        ccf.setUsername(conConfig.getUsername());
        ccf.setPassword(conConfig.getPassword());
        return ccf;
    }
    	
	@Override
	public void run() {
		try{
	    	if (qName == null) {
	    		return;
	    	}
			SimpleMessageListenerContainer smlc = new SimpleMessageListenerContainer(connectionFactory());
			smlc.setConnectionFactory(connectionFactory());
			smlc.setQueueNames(qName);
			smlc.setMessageListener(new MessageListenerAdapter(new MessageListener(ctx)));
			smlc.start();
			
		} catch (Exception e) {
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new ReadMQMessage(qName, ctx));
		}
		
	}

}
