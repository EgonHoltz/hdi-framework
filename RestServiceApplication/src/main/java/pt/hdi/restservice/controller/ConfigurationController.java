package pt.hdi.restservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.MQConfig;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.repository.ApplicationRepository;
import pt.hdi.restservice.repository.ConfigurationRepository;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.ConfigurationService;
import pt.hdi.restservice.service.DocumentService;


@RestController
public class ConfigurationController {

    @Autowired
    private DocumentRepository docRep;

    @Autowired
    private ApplicationRepository appRep;

    @Autowired
    private ConfigurationService confSvc;

    @Autowired
    private ConfigurationRepository confRep;
	
	@GetMapping("admin/config")
	public List<Configuration> getAllConfiguration(){
		return confSvc.getAllConfigs();
	}
	
	@PostMapping("admin/config")
	public ResponseEntity<Void> createNewConfiguration(@RequestBody Configuration config){
		boolean createdOk = false;
		createdOk = confSvc.createNewConfiguration(config);
		if (createdOk) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}
		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("admin/config/sftp/{configId}")
	public ResponseEntity<Void> createSftpConfiguration(@PathVariable String configId, @RequestBody SFTPConfig sftpConfig){
		try {
			Configuration config = confSvc.getByDocumentConfiguration(configId);
			config.addSftpConfig(sftpConfig);
			boolean createdOk = confSvc.saveConfiguration(config);
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
	
	@DeleteMapping("admin/config/sftp/{configId}")
	public ResponseEntity<Void> removeSftpConfiguration(@PathVariable String configId, @RequestBody SFTPConfig sftpConfig){
		try {
			Configuration config = confSvc.getByDocumentConfiguration(configId);
			boolean removedOk = config.removeSftpConfig(sftpConfig);
			removedOk &= confSvc.saveConfiguration(config);
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
	

    /**
     * Document Application association - Create technology behind the document, which allows the communication
     * between other applications
     * 
     * URLs:
     * GET  /document/{documentId}/application/{applicationId}
     * 
     */

     @GetMapping("document/{documentId}/application/{applicationId}")     
     public ResponseEntity getAllApplicationsAssociatedWithDocument(@PathVariable String documentId, @PathVariable String applicationId){
        System.out.println("Called getAllApplicationsAssociatedWithDocument " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() && !doc.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());

        if (conf != null){
            return new ResponseEntity<>(conf,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }


    /**
     * Document Application association - Create technology behind the document, which allows the communication
     * between other applications
     * 
     * URLs:
     * GET  /document/{documentId}/application/{applicationId}/mqqueue
     * PUT  /document/{documentId}/application/{applicationId}/mqqueue
     * 
     */

     @GetMapping("document/{documentId}/application/{applicationId}/mqqueue")     
     public ResponseEntity getAssociationMQConfiguration(@PathVariable String documentId, @PathVariable String applicationId){
        System.out.println("Called getAssociationMQConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());

        if (conf != null){
            return new ResponseEntity<>(conf.getMqConfig(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

     }

     @PutMapping("document/{documentId}/application/{applicationId}/mqqueue")     
     public ResponseEntity changeAssociationMQConfiguration(@PathVariable String documentId, 
        @PathVariable String applicationId, @RequestBody List<MQConfig> mqConfig){
        System.out.println("Called getAllApplicationsAssociatedWithDocument " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (mqConfig == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());
        if (conf == null) {
            conf = new Configuration(doc.get(),app.get());
            confRep.save(conf);
        }
        HttpStatus rtnHttp = HttpStatus.BAD_REQUEST;
        for (MQConfig mc : mqConfig) {
            
            List<MQConfig> currMqConfig = null;

            if (conf.getMqConfig() == null){
                currMqConfig = new ArrayList<>();
                conf.setMqConfig(currMqConfig);
            } else {
                currMqConfig = conf.getMqConfig();
            }

            boolean hasConfig = currMqConfig.stream().anyMatch(c -> c.getDirection().equals(mc.getDirection()));
            if (hasConfig){
                Optional<MQConfig> mqConfigOld = currMqConfig.stream().filter(c -> c.getDirection().equals(mc.getDirection())).findFirst();
                BeanUtils.copyProperties(mc, mqConfigOld.get(), ObjectHelper.getNullPropertyNames(mqConfigOld.get()));
                conf.addMqConfig(mc);
                rtnHttp = HttpStatus.OK;
            } else {
                conf.addMqConfig(mc);
                rtnHttp = HttpStatus.CREATED;
            }
            
        }

        confRep.save(conf);

        return new ResponseEntity<>(rtnHttp);
     }
         /**
     * Document Application association - Create technology behind the document, which allows the communication
     * between other applications
     * 
     * URLs:
     * GET  /document/{documentId}/application/{applicationId}/sftp
     * PUT  /document/{documentId}/application/{applicationId}/sftp
     * 
     */

     @GetMapping("document/{documentId}/application/{applicationId}/sftp")     
     public ResponseEntity getAssociationSFTPConfiguration(@PathVariable String documentId, @PathVariable String applicationId){
        System.out.println("Called getAssociationMQConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());

        if (conf != null){
            return new ResponseEntity<>(conf.getSftpConfig(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

     }

     @PutMapping("document/{documentId}/application/{applicationId}/sftp")     
     public ResponseEntity changeAssociationSFTPConfiguration(@PathVariable String documentId, 
        @PathVariable String applicationId, @RequestBody List<SFTPConfig> sftpConfig){
        System.out.println("Called changeAssociationSFTPConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (sftpConfig == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());
        if (conf == null) {
            conf = new Configuration(doc.get(),app.get());
            confRep.save(conf);
        }
        HttpStatus rtnHttp = HttpStatus.BAD_REQUEST;
        for (SFTPConfig sftp : sftpConfig) {
            
            List<SFTPConfig> currSftpConfig = null;

            if (conf.getSftpConfig() == null){
                currSftpConfig = new ArrayList<>();
                //conf.setSftpConfig(mqConfig);
            } else {
                currSftpConfig = conf.getSftpConfig();
            }

            boolean hasConfig = currSftpConfig.stream().anyMatch(c -> c.getDirection().equals(sftp.getDirection()));
            if (hasConfig){
                Optional<SFTPConfig> mqConfigOld = currSftpConfig.stream().filter(c -> c.getDirection().equals(sftp.getDirection())).findFirst();
                BeanUtils.copyProperties(sftp, mqConfigOld.get(), ObjectHelper.getNullPropertyNames(mqConfigOld.get()));
                conf.addSftpConfig(sftp);
                rtnHttp = HttpStatus.OK;
            } else {
                conf.addSftpConfig(sftp);
                rtnHttp = HttpStatus.CREATED;
            }
            
        }

        confRep.save(conf);

        return new ResponseEntity<>(rtnHttp);
     }


}
