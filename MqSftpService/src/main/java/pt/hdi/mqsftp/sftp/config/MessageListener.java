package pt.hdi.mqsftp.sftp.config;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements org.springframework.amqp.core.MessageListener{

	@Override
	public void onMessage(Message message) {
        String body = new String(message.getBody());
        System.out.println("Received message: " + body);
	}

}
