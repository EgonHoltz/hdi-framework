package pt.hdi.mqsftp.sftp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.mqsftp.sftp.model.Configuration;
import pt.hdi.mqsftp.sftp.service.ConfigurationService;

@RestController
@RequestMapping("/admin")
public class ConfigurationController {

	@Autowired
	private ConfigurationService cnfgSrvc;
	
	@GetMapping("/config")
	public List<Configuration> getAllConfiguration(){
		return cnfgSrvc.getAllConfigs();
	}
	
	@PostMapping("/config")
	public ResponseEntity<Void> createNewConfiguration(@RequestBody Configuration config){
		boolean createdOk = false;
		createdOk = cnfgSrvc.createNewConfiguration(config);
		if (createdOk) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
}
