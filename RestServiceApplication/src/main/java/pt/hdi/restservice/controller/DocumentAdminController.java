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

import pt.hdi.restservice.Utils.ApplicationEnums.DOCUMENT_STATUS;
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

    /**
     * Document - Work on its name, who is responsible, parameters and observations
     * 
     * URLs:
     * GET  /document/
     * GET  /document/{documentId}
     * POST /document/
     * PUT  /document/{documentId}
     * GET  /document/{documentId}/dbStatus
     * POST /document/{documentId}/dbStatus
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

    @GetMapping("/{id}/dbStatus")
    public ResponseEntity getDocumentDbStatus(@PathVariable String id){
        System.out.println("Called getDocumentDbStatus");
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<DocumentData> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DOCUMENT_STATUS status = docSvc.getDocumentStatusOnDb(docFound.get());
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/{id}/dbStatus")
    public ResponseEntity pushDocumentUpdateToDb(@PathVariable String id){
        System.out.println("Called pushDocumentUpdateToDb " + id);
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<DocumentData> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean status = docSvc.generateDocumentOnDb(docFound.get());

        if(status){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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



}
