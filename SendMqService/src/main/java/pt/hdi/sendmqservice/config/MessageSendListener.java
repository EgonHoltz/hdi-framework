package pt.hdi.sendmqservice.config;

import org.springframework.amqp.core.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import pt.hdi.sendmqservice.bean.MQConnectionBean;
import pt.hdi.sendmqservice.model.Configuration;
import pt.hdi.sendmqservice.service.ConfigurationService;
import pt.hdi.sendmqservice.service.DataCentralizerService;
import pt.hdi.sendmqservice.service.MQWriterService;
import pt.hdi.sendmqservice.service.SendResponseService;

@Component
public class MessageSendListener implements org.springframework.amqp.core.MessageListener{

	private ApplicationContext ctx;
	
	private SendResponseService srService;
	
	private ConfigurationService confService;

	private MQWriterService mqResService;
	
	public MessageSendListener(ApplicationContext ctx) {
		this.ctx = ctx;
		this.confService = ctx.getBean(ConfigurationService.class);
		this.srService = ctx.getBean(SendResponseService.class);
		this.mqResService = ctx.getBean(MQWriterService.class);
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
		Environment env = ctx.getEnvironment();
		MQConnectionBean conConfig = new MQConnectionBean(env);
		if (confService.isValidQueryAndQueue(conf, rcvId, body)){
			String response = srService.sendData(conf.getDocumentDataName().replaceAll("\\s", "").toLowerCase(), body);
			mqResService.sendMQMessageByQueue(rcvId, conConfig, response);
		} else {
			mqResService.sendMQMessageByQueue(rcvId, conConfig, "NOT VALID REQUEST");
		}
	}

}
