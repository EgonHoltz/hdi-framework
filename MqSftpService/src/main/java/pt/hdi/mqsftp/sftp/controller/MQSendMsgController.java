package pt.hdi.mqsftp.sftp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.service.MQWriterService;

@RestController
public class MQSendMsgController {

	@Autowired
	private MQWriterService mqService;
	
	@GetMapping(path = "/mqsend/{qname}")
	public ResponseEntity<Void> sendMsgByMQ(@PathVariable String qName){
		MQConnectionBean conConfig = new MQConnectionBean("locahost", "quser", "qpass",8042);
		boolean success = mqService.sendMQMessageByQueue(qName, conConfig, "blablabla");
		if(success) {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}

}
