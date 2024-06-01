package pt.hdi.restservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.FileAuditLogger;
import pt.hdi.restservice.repository.ApplicationRepository;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.AuditLoggerService;
import pt.hdi.restservice.service.ConfigurationService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/auditLogger")
public class AuditLoggerController {

    @Autowired
    private AuditLoggerService auditLogSvc;

    @Autowired
    private ApplicationRepository appRep;

    @Autowired
    private DocumentRepository docRep;

    @Autowired
    private ConfigurationService confSvc;

    @GetMapping("/sftp/scheduler")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/sftp/scheduler")
    public ResponseEntity createAuditLoggerSftpScheduler(@RequestBody FileAuditLogger fileAuditLogger) {
        System.out.println("Called createAuditLoggerSftpScheduler " + fileAuditLogger.getFileName());
        if (fileAuditLogger == null || fileAuditLogger.getConfigurationId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        FileAuditLogger newFileAuditLogger = auditLogSvc.addNewFileAuditLogger(fileAuditLogger);
        if (newFileAuditLogger == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newFileAuditLogger,HttpStatus.CREATED);
    }

    @GetMapping("/document/{documentId}/application/{applicationId}")    
    public ResponseEntity findAllFileAuditLoggersByConfiguration(@PathVariable String documentId, 
        @PathVariable String applicationId){
        System.out.println("Called findAllFileAuditLoggersByConfiguration " + documentId + ", " + applicationId);

        Optional<Application> app = appRep.findById(applicationId);
        Optional<DocumentData> doc = docRep.findById(documentId);

        if (!app.isPresent() || !doc.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Configuration conf = confSvc.getConfigurationByDocApp(doc.get(),app.get());
        if (conf == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<FileAuditLogger> audits = auditLogSvc.getAllFileAuditLoggerByConfiguration(conf);
        

        return new ResponseEntity<>(audits,HttpStatus.OK);
    }
    
    
    
}
