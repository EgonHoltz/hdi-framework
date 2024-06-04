package pt.hdi.restservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import pt.hdi.restservice.Utils.ApplicationEnums.SEND_SFTP_STATUS;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.FileAuditLogger;
import pt.hdi.restservice.repository.ApplicationRepository;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.AuditLoggerService;
import pt.hdi.restservice.service.ConfigurationService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




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
    
    @GetMapping("/sftp/filesNotSent")
    public ResponseEntity findAllFilesToStartPreparation(){
        System.out.println("Called findAllFilesToStartPreparation ");

        List<FileAuditLogger> filesToPrepare = auditLogSvc.getAllFilesOnFileGenerateStatus();

        if (filesToPrepare == null || CollectionUtils.isEmpty(filesToPrepare)){
            return new ResponseEntity<>(HttpStatus.OK);
        }  

        for (FileAuditLogger audit : filesToPrepare){
            auditLogSvc.moveStatus(audit, SEND_SFTP_STATUS.PREPARING_SFTP);
        }

        return new ResponseEntity<>(filesToPrepare,HttpStatus.OK);
    }

    @GetMapping("/sftp")
    public ResponseEntity findFileAuditLoggerByFileName(@RequestParam String fileName){
        try {
            String decodedFileName = UriUtils.decode(fileName, "UTF-8");
            System.out.println("Called findFileAuditLoggerByFileName " + decodedFileName);
            FileAuditLogger fileAudit = auditLogSvc.getAuditByFileName(decodedFileName);

            if (fileAudit == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }  

            return new ResponseEntity<>(fileAudit,HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/sftp/filesNotSent/sent/{auditId}")
    public ResponseEntity moveFileStatusToSent(@PathVariable String auditId) {
        System.out.println("Called findAllFilesToStartPreparation ");
        
        FileAuditLogger audit = auditLogSvc.getAuditById(auditId);

        if (audit == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        auditLogSvc.moveStatus(audit, SEND_SFTP_STATUS.SENT);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
