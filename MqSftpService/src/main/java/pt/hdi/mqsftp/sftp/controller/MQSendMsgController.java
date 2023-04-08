package pt.hdi.mqsftp.sftp.controller;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.service.MQWriterService;

@RestController
public class MQSendMsgController {

    @Autowired
    private AmqpTemplate amqpTemplate;
	
	@PostMapping(path = "/mqsend/{qName}")
	public ResponseEntity<Void> sendMsgByMQ(@PathVariable String qName, @RequestBody String msg){
		try {
			if (msg == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);				
			}
			amqpTemplate.convertAndSend(qName,msg);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (AmqpException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 		
	}

}
