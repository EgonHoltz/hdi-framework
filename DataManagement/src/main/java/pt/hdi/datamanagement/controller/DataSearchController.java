package pt.hdi.datamanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.datamanagement.bean.DocumentFilterFields;
import pt.hdi.datamanagement.model.Structure;
import pt.hdi.datamanagement.service.DataService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/search")
public class DataSearchController {

    @Autowired
    private DataService dataSrc;

    @PostMapping("/query")
    public ResponseEntity getCollectionData(@RequestParam String collectionName,
        @RequestBody List<DocumentFilterFields> filters,@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){

        System.out.println("Called getCollectionData " );
        if (collectionName == null || filters == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<DocumentFilterFields> serializedFilters = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(filters);
            serializedFilters = objectMapper.readValue(jsonString, 
                new TypeReference<List<DocumentFilterFields>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Page<Document> data = dataSrc.getCollectionData(collectionName,serializedFilters, page, size);
        
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping("delete/{dataId}")
    public ResponseEntity deleteRecordOnCollection(@PathVariable String dataId,
            @RequestParam String collectionName){
        
        if (collectionName == null || dataId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return dataSrc.deleteDocumentById(collectionName, dataId);
    }

}
