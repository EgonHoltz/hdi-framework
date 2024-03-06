package pt.hdi.sendmqservice.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import pt.hdi.sendmqservice.bean.MQConnectionBean;
import pt.hdi.sendmqservice.config.MessageSendListener;

//@Component
public class ResponserMQMessage implements Runnable{

	private ApplicationContext ctx;
	
	
	public ResponserMQMessage() {
		super();
	}

	public ResponserMQMessage(String queueName, ApplicationContext ctx) {
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
			System.out.println("Created new Thread!! " + qName);
			SimpleMessageListenerContainer smlc = new SimpleMessageListenerContainer(connectionFactory());
			smlc.setConnectionFactory(connectionFactory());
			smlc.setQueueNames(qName);
			smlc.setMessageListener(new MessageListenerAdapter(new MessageSendListener(ctx)));
			smlc.start();
			
		} catch (Exception e) {
			System.out.println("I finished the thread with exception:");
			e.printStackTrace();
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new ReadMQMessage(qName, ctx));
		}
		
	}

}
