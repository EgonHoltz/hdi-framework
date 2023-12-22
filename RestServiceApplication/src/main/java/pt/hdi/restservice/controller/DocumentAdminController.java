package pt.hdi.restservice.controller;

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
import pt.hdi.restservice.model.Document;
import pt.hdi.restservice.repository.DocumentRepository;

@RestController
@RequestMapping("/document")
public class DocumentAdminController {

    @Autowired
    private DocumentRepository docRep;

    @GetMapping("/")
    public List<Document> getAllDocuments(){
            System.out.println("Called getAllApplications");
            return docRep.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getDocumentById(@PathVariable String id){
        System.out.println("Called getDocumentById");
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Document> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(docFound.get(), HttpStatus.OK);
}


    @PostMapping("/")
    public ResponseEntity createDocument(@RequestBody Document doc){
        System.out.println("Called createDocument");
        if (doc == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Document newDoc = new Document(doc);
        newDoc = docRep.save(newDoc);
        if (newDoc != null){
            return new ResponseEntity<>(newDoc, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity changeApplication(@PathVariable String id, @RequestBody Document doc){
        System.out.println("Called changeApplication " + id);
        if (id == null || doc == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Document> docFound = docRep.findById(id);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Document docOld = docFound.get();
        BeanUtils.copyProperties(doc, docOld, ObjectHelper.getNullPropertyNames(docOld));
        docRep.save(doc);

        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    

}
