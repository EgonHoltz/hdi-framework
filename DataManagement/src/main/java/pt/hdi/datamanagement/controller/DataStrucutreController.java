package pt.hdi.datamanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.datamanagement.model.Structure;
import pt.hdi.datamanagement.service.DataService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/collection")
public class DataStrucutreController {

    @Autowired
    private DataService dataSrc;

    /**
     * Data - work on document structure to be used with received data
     * 
     * URLs:
     * GET  /collection (string)
     * GET  /collection/structure (string,string)
     * POST /collection/structure (string)
     * PUT  /collection/structure (string,string)
     * 
     */
    @GetMapping("/structure")
    public ResponseEntity getCollectionFields(@RequestParam String collectionName, @RequestParam String modelKey){
        System.out.println("Called getCollectionFields " );
        if (collectionName == null || modelKey == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        List<String> structure = dataSrc.getCollectionFields(collectionName, modelKey);
        
        return new ResponseEntity<>(structure, HttpStatus.OK);
    }
    
    @GetMapping("/")
    public ResponseEntity verifyCollectionExists(@RequestParam String collectionName){
        System.out.println("Called verifyCollectionExists " );
        if (collectionName == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return dataSrc.verifyCollectionExists(collectionName);
    }

    @PostMapping("/structure")
    public ResponseEntity doCollectionInsert(@RequestParam String collectionName, 
    @RequestBody List<Structure> structure){
        System.out.println("Called doCollectionInsert " );
        if (structure == null || CollectionUtils.isEmpty(structure) || collectionName == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return dataSrc.doCollectionInsert(structure, collectionName);
    }
    
    @PutMapping("/structure")
    public ResponseEntity doCollectionEdit(@RequestParam String collectionName, @RequestParam String modelKey,
    @RequestBody Structure structure){
        System.out.println("Called doCollectionEdit " );
        
        if (structure == null || collectionName == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return dataSrc.addDummyAttribute(collectionName, modelKey, structure);
    }
}
