package pt.hdi.restservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.bean.ConfigurationSFTPSchedulerBean;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.GRPCConfig;
import pt.hdi.restservice.model.MQConfig;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.model.SftpFileSchedulerConfig;
import pt.hdi.restservice.repository.ApplicationRepository;
import pt.hdi.restservice.repository.ConfigurationRepository;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.ConfigurationService;
import pt.hdi.restservice.service.RabbitMQService;


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

    @Autowired
    private RabbitMQService rabbitSvc;
	
    /**
     * Configuration - Get all configurations
     * 
     * URLs:
     * GET  /configuration
     * GET  /configuration/mqqueue
     * GET  /configuration/mqqueue/{mqName}
     * POST /configuration/mqqueue/{mqName}
     * GET  /configuration/sftp
     * GET  /configuration/sftp/schedulers
     * GET  /configuration/sftp/{filename}
     * GET  /configuration/grpc
     * GET  /configuration/grpc/{mqName}
     * 
     */

     @GetMapping("/configuration/{configId}")
     public ResponseEntity getMethodName(@RequestParam String configId) {
        System.out.println("Called getMethodName ");
        return new ResponseEntity<>(confSvc.getConfigurationById(configId), HttpStatus.OK);
     }
     

	@GetMapping("configuration/mqqueue")
	public ResponseEntity getAllMqConfiguration(){
        System.out.println("Called getAllConfiguration ");
		return new ResponseEntity<>(confSvc.getAllMqConfigs(),HttpStatus.OK);
	}
	
	@GetMapping("configuration/mqqueue/notstarted")
	public ResponseEntity getMqConfigurationNotStarted(){
        System.out.println("Called getMqConfigurationNotStarted ");
		return new ResponseEntity<>(confSvc.getAllConfigurationWithMQAndNotStarted(),HttpStatus.OK);
	}

	@GetMapping("configuration/mqqueue/{mqName}")
	public ResponseEntity getMqConfigurationByMqName(@PathVariable String mqName){
        System.out.println("Called getMqConfigurationByMqName " + mqName);
		return new ResponseEntity<>(confSvc.getByRqName(mqName),HttpStatus.OK);
	}

    @PutMapping("configuration/mqqueue/{mqName}")
    public ResponseEntity putMethodName(@PathVariable String mqName, @RequestBody String entity) {
        System.out.println("Called putMethodName " + mqName);
        try{
            confSvc.setMqQueueStarted(mqName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

	@GetMapping("configuration/sftp")
	public ResponseEntity getAllSftpConfiguration(){
        System.out.println("Called getAllSftpConfiguration ");
		return new ResponseEntity<>(confSvc.getAllSftpConfigs(),HttpStatus.OK);
	}

	@GetMapping("configuration/sftp/schedulers")
	public ResponseEntity getAllSftpSchedulersConfiguration(){
        System.out.println("Called getAllSftpSchedulersConfiguration ");
		return new ResponseEntity<>(confSvc.getAllSftpSchedulerConfigs(),HttpStatus.OK);
	}

	@GetMapping("configuration/sftp/{fileName}")
	public ResponseEntity getSftpConfigurationByFileName(@PathVariable String fileName){
        System.out.println("Called getSftpConfigurationByFileName " + fileName);
		return new ResponseEntity<>(confSvc.getByFileName(fileName),HttpStatus.OK);
	}

    @GetMapping("configuration/sftp/{configurationId}/scheduler")
	public ResponseEntity getSftpConfigurationLastIdForDelta(@PathVariable String configurationId){
        System.out.println("Called getSftpConfigurationLastIdForDelta " + configurationId);
		return new ResponseEntity<>(confSvc.getSftpSchedulerDeltaId(configurationId),HttpStatus.OK);
	}

    @PutMapping("configuration/sftp/{configurationId}/scheduler")
	public ResponseEntity<String> updateSftpConfigurationLastIdForDelta(@PathVariable String configurationId, @RequestParam String lastDocId){
        System.out.println("Called updateSftpConfigurationLastIdForDelta " + configurationId);
        ConfigurationSFTPSchedulerBean rtn = confSvc.updateSftpSchedulerDeltaId(configurationId, lastDocId);
        if (rtn == null) {
            return new ResponseEntity<>("NOK",HttpStatus.BAD_REQUEST);
        }
		return ResponseEntity.ok("OK");
	}

    @GetMapping("configuration/grpc")
	public ResponseEntity getAllGrpcConfiguration(){
        System.out.println("Called getAllGrpcConfiguration ");
		return new ResponseEntity<>(confSvc.getAllGrpcConfigs(),HttpStatus.OK);
	}

	@GetMapping("configuration/grpc/{cliendId}")
	public ResponseEntity getGrpcConfigurationByFileName(@PathVariable String cliendId){
        System.out.println("Called getGrpcConfigurationByFileName " + cliendId);
		return new ResponseEntity<>(confSvc.getByGrpcClientId(cliendId),HttpStatus.OK);
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
     * Configuration - Get all configurations
     * 
     * URLs:
     * GET  /scheduler/sftp/application/{applicationId}
     * GET  /scheduler/application/{applicationId}/document/{documentId}
     * PUT  /scheduler/application/{applicationId}/document/{documentId}
     * 
     */

     @GetMapping("scheduler/sftp/application/{applicationId}")     
     public ResponseEntity getDocumentsByApplicationOnSftp(@PathVariable String applicationId){
        System.out.println("Called getDocumentsByApplicationOnSftp " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);

        if (!app.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<DocumentData> docs = confSvc.getDocumentDataByApplicationWithSftp(applicationId);

        if (docs != null){
            return new ResponseEntity<>(docs,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }

     @GetMapping("scheduler/application/{applicationId}/document/{documentId}")     
     public ResponseEntity getSftpSchedulerConfiguration(@PathVariable String applicationId,
     @PathVariable String documentId){
        System.out.println("Called getSftpSchedulerConfiguration " + applicationId);

        if (applicationId == null || documentId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Configuration config = confRep.findByDocumentApplication(documentId, applicationId);


        if (config == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SftpFileSchedulerConfig sftpSchdlrConfig = config.getSftpSchedulerConfig();

        if (sftpSchdlrConfig == null){
            return new ResponseEntity<>(new SftpFileSchedulerConfig(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(config.getSftpSchedulerConfig(),HttpStatus.OK);
        }
     }

     @PutMapping("scheduler/application/{applicationId}/document/{documentId}")     
     public ResponseEntity upsertSftpSchedulerConfiguration(@PathVariable String applicationId,
     @PathVariable String documentId, @RequestBody SftpFileSchedulerConfig sftpSchedulerConfig){
        System.out.println("Called upsertSftpSchedulerConfiguration " + applicationId);

        if (applicationId == null || documentId == null || sftpSchedulerConfig == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Configuration config = confRep.findByDocumentApplication(documentId, applicationId);


        if (config == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try{
            confSvc.upsertSftpSendFileConfig(config, sftpSchedulerConfig);
            
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
                String currentPassword = mqConfigOld.get().getPassword();
                if (!mqConfigOld.get().getUser().equals(mc.getUser())){
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                BeanUtils.copyProperties(mc, mqConfigOld.get(), ObjectHelper.getNullPropertyNames(mqConfigOld.get()));
                // Also changes the password on the MQ server
                if (!currentPassword.equals(mc.getPassword())){
                    rtnHttp = rabbitSvc.createUser(mc.getUser(), mc.getPassword()).getStatusCode();
                    if (!HttpStatus.OK.equals(rtnHttp) && !HttpStatus.CREATED.equals(rtnHttp)){
                        throw new HttpClientErrorException(rtnHttp);
                    }
                }
                confSvc.updateMqOnConfiguration(conf, mc);
                rtnHttp = HttpStatus.OK;
            } else {
                rtnHttp = rabbitSvc.createUser(mc.getUser(), mc.getPassword()).getStatusCode();
                if (!HttpStatus.OK.equals(rtnHttp) && !HttpStatus.CREATED.equals(rtnHttp)){
                    throw new HttpClientErrorException(rtnHttp);
                }
                rtnHttp = rabbitSvc.createQueue(mc.getMqName()).getStatusCode();
                if (!HttpStatus.OK.equals(rtnHttp) && !HttpStatus.CREATED.equals(rtnHttp)){
                    throw new HttpClientErrorException(rtnHttp);
                }
                rtnHttp = rabbitSvc.addPermission(mc.getUser(), mc.getMqName()).getStatusCode();
                if (!HttpStatus.OK.equals(rtnHttp) && !HttpStatus.CREATED.equals(rtnHttp)){
                    throw new HttpClientErrorException(rtnHttp);
                }
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
        System.out.println("Called getAssociationSFTPConfiguration " + documentId + ", " + applicationId);

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

    /**
     * Document Application association - Create technology behind the document, which allows the communication
     * between other applications
     * 
     * URLs:
     * GET  /document/{documentId}/application/{applicationId}/grpc
     * PUT  /document/{documentId}/application/{applicationId}/grpc
     * 
     */

     @GetMapping("document/{documentId}/application/{applicationId}/grpc")     
     public ResponseEntity getAssociationGRPCConfiguration(@PathVariable String documentId, @PathVariable String applicationId){
        System.out.println("Called getAssociationGRPCConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());

        if (conf != null){
            return new ResponseEntity<>(conf.getGrpcConfig(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

     }

     @PutMapping("document/{documentId}/application/{applicationId}/grpc")     
     public ResponseEntity changeAssociationGRPCConfiguration(@PathVariable String documentId, 
        @PathVariable String applicationId, @RequestBody List<GRPCConfig> grpcConfig){
        System.out.println("Called changeAssociationGRPCConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (grpcConfig == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());
        if (conf == null) {
            conf = new Configuration(doc.get(),app.get());
            confRep.save(conf);
        }
        HttpStatus rtnHttp = HttpStatus.BAD_REQUEST;
        for (GRPCConfig grpc : grpcConfig) {
            
            List<GRPCConfig> currGrpcConfig = null;

            if (conf.getGrpcConfig() == null){
                currGrpcConfig = new ArrayList<>();
            } else {
                currGrpcConfig = conf.getGrpcConfig();
            }

            boolean hasConfig = currGrpcConfig.stream().anyMatch(c -> c.getDirection().equals(grpc.getDirection()));
            if (hasConfig){
                Optional<GRPCConfig> grpcConfigOld = currGrpcConfig.stream().filter(c -> c.getDirection().equals(grpc.getDirection())).findFirst();
                BeanUtils.copyProperties(grpc, grpcConfigOld.get(), ObjectHelper.getNullPropertyNames(grpcConfigOld.get()));
                conf.addGrpcConfig(grpc);
                rtnHttp = HttpStatus.OK;
            } else {
                conf.addGrpcConfig(grpc);
                rtnHttp = HttpStatus.CREATED;
            }
            
        }

        confRep.save(conf);

        return new ResponseEntity<>(rtnHttp);
     }

}
