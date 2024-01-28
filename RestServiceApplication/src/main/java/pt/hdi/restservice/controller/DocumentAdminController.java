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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.MQConfig;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.model.Structure;
import pt.hdi.restservice.repository.ApplicationRepository;
import pt.hdi.restservice.repository.ConfigurationRepository;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.ConfigurationService;
import pt.hdi.restservice.service.DocumentService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/document")
public class DocumentAdminController {

    @Autowired
    private DocumentRepository docRep;

    @Autowired
    private DocumentService docSvc;

    @Autowired
    private ApplicationRepository appRep;

    @Autowired
    private ConfigurationService confSvc;

    @Autowired
    private ConfigurationRepository confRep;

    /**
     * Document - Work on its name, who is responsible, parameters and observations
     * 
     * URLs:
     * GET  /document/
     * GET  /document/{documentId}
     * POST /document/
     * PUT  /document/{documentId}
     */

    @GetMapping("/")
    public List<DocumentData> getAllDocuments(){
            System.out.println("Called getAllApplications");
            return docRep.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getDocumentById(@PathVariable String id){
        System.out.println("Called getDocumentById");
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<DocumentData> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(docFound.get(), HttpStatus.OK);
}


    @PostMapping("/")
    public ResponseEntity createDocument(@RequestBody DocumentData doc){
        System.out.println("Called createDocument");
        if (doc == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DocumentData newDoc = new DocumentData(doc);
        newDoc = docRep.save(newDoc);
        if (newDoc != null){
            return new ResponseEntity<>(newDoc, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity changeDocument(@PathVariable String id, @RequestBody DocumentData doc){
        System.out.println("Called changeDocument " + id);
        if (id == null || doc == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<DocumentData> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DocumentData docOld = docFound.get();
        doc.setStructures(docOld.getStructures());
        BeanUtils.copyProperties(doc, docOld, ObjectHelper.getNullPropertyNames(docOld));
        docRep.save(doc);

        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    /**
     * Structure of document - Work on field , field type and configuration that the
     * document could have
     * 
     * URLs:
     * GET  /document/structure/{documentId}
     * POST /document/structure/{documentId}
     * PUT  /document/structure/{documentId}
     * 
     */
    @GetMapping("/structure/{id}")
    public ResponseEntity getDocumentStructureById(@PathVariable String id){
        System.out.println("Called getDocumentStructureById");
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<DocumentData> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(docFound.get().getStructures(), HttpStatus.OK);
    }

    @PostMapping("/structure/{id}")
    public ResponseEntity createDocumentStructure(@PathVariable String id, @RequestBody Structure structure){
        System.out.println("Called createDocumentStructure");
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean result = docSvc.addStructure(id, structure);

        if (result){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/structure/{id}")
    public ResponseEntity changeDocumentStructure(@PathVariable String id, @RequestBody Structure structure){
        System.out.println("Called changeDocumentStructure " + id);
        if (id == null || structure == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean result = docSvc.editStructureField(id, structure);

        if (result){
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    /**
     * Document Application association - Create technology behind the document, which allows the communication
     * between other applications
     * 
     * URLs:
     * GET  /document/{documentId}/application/{applicationId}
     * 
     */

     @GetMapping("/{documentId}/application/{applicationId}")     
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

     @GetMapping("/{documentId}/application/{applicationId}/mqqueue")     
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

     @PutMapping("/{documentId}/application/{applicationId}/mqqueue")     
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

     @GetMapping("/{documentId}/application/{applicationId}/sftp")     
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

     @PutMapping("/{documentId}/application/{applicationId}/sftp")     
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
