package pt.hdi.mqservice.config;

import org.springframework.amqp.core.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import pt.hdi.mqservice.model.Configuration;
import pt.hdi.mqservice.service.ConfigurationService;
import pt.hdi.mqservice.service.DataCentralizerService;

@Component
public class MessageListener implements org.springframework.amqp.core.MessageListener{

	private ApplicationContext ctx;
	
	private DataCentralizerService dcService;
	
	private ConfigurationService confService;
	
	public MessageListener(ApplicationContext ctx) {
		this.ctx = ctx;
		this.confService = ctx.getBean(ConfigurationService.class);
		this.dcService = ctx.getBean(DataCentralizerService.class);
	}

	@Override
	public void onMessage(Message message) {
        String body = new String(message.getBody());
        String rcvId= message.getMessageProperties().getConsumerQueue();
        Configuration conf = confService.getByRqName(rcvId);
        if (conf !=null && !body.isEmpty()) {
        	System.out.println("getConsumerQueue "+rcvId);
        	System.out.println("Received message: " + body);
        	ResponseEntity<String> res = dcService.sendMessageToCentralizedServcer(conf, body);
        	if (res != null &&
        			HttpStatus.ACCEPTED.equals(res.getStatusCode())) {
        		System.out.println("Message sent to centralization");
        	} else {
        		System.out.println("Something went wrong on msg fw");
        	}
        }
	}

}
