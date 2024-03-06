package pt.hdi.sendmqservice.config;

import org.springframework.amqp.core.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import pt.hdi.sendmqservice.model.Configuration;
import pt.hdi.sendmqservice.service.ConfigurationService;
import pt.hdi.sendmqservice.service.DataCentralizerService;

@Component
public class MessageReceiveListener implements org.springframework.amqp.core.MessageListener{

	private ApplicationContext ctx;
	
	private DataCentralizerService dcService;
	
	private ConfigurationService confService;
	
	public MessageReceiveListener(ApplicationContext ctx) {
		this.ctx = ctx;
		this.confService = ctx.getBean(ConfigurationService.class);
		this.dcService = ctx.getBean(DataCentralizerService.class);
	}

	@Override
	public void onMessage(Message message) {
        String body = new String(message.getBody());
        String rcvId= message.getMessageProperties().getConsumerQueue();
		ResponseEntity resConf = confService.getByRqName(rcvId);
		if (!resConf.getStatusCode().equals(HttpStatus.OK)){
			System.out.println("Configuration not found for: " + rcvId);
			throw new HttpClientErrorException(resConf.getStatusCode());
		}
		Configuration conf = (Configuration) resConf.getBody();
		if (confService.isValidMessageAndQueue(conf, rcvId, body)){
			dcService.sendMessage(conf, body);
		} else {
			dcService.sendRejectedMessage(conf, body);
		}
	}

}
