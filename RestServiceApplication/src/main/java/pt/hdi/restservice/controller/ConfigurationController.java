package pt.hdi.restservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.service.ConfigurationService;


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
	
	@PostMapping("/config/sftp/{configId}")
	public ResponseEntity<Void> createSftpConfiguration(@PathVariable String configId, @RequestBody SFTPConfig sftpConfig){
		try {
			Configuration config = cnfgSrvc.getByDocumentName(configId);
			config.addMqConfig(sftpConfig);
			boolean createdOk = cnfgSrvc.saveConfiguration(config);
			if (createdOk) {
				return new ResponseEntity<Void>(HttpStatus.CREATED);
			}		
		} catch (Exception e) {
			System.err.println("Error: "+e.getStackTrace());
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping("/config/sftp/{configId}")
	public ResponseEntity<Void> removeSftpConfiguration(@PathVariable String configId, @RequestBody SFTPConfig sftpConfig){
		try {
			Configuration config = cnfgSrvc.getByDocumentName(configId);
			boolean removedOk = config.removeSftpConfig(sftpConfig);
			removedOk &= cnfgSrvc.saveConfiguration(config);
			if (removedOk) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}		
		} catch (Exception e) {
			System.err.println("Error: "+e.getStackTrace());
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
}
