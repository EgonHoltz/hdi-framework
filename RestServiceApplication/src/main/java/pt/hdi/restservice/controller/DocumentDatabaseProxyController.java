package pt.hdi.restservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.bean.DocumentFilterFields;
import pt.hdi.restservice.bean.PaginationResponse;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.repository.DocumentRepository;
import pt.hdi.restservice.service.DataManagementService;

@RestController
@RequestMapping("/document/db/")
public class DocumentDatabaseProxyController {

    @Autowired
    private DataManagementService docDbSvc;

    @Autowired
    private DocumentRepository docRep;

    /**
     * Document - Work on its name, who is responsible, parameters and observations
     * 
     * URLs:
     * GET  /document/db/{docId}
     */

    @PostMapping("/{docId}")
    public ResponseEntity<PaginationResponse<Document>> getDbDataOfCollectionWithPagination(@PathVariable String docId, 
        @RequestBody List<DocumentFilterFields> filters,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){

        System.out.println("Called getDbDataOfCollectionWithPagination");

        if (docId == null || filters == null){
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

        List<DocumentFilterFields> filledFilters = serializedFilters.stream()
                      .filter(f -> f.getValue() != null && !f.getValue().trim().isEmpty())
                      .map(f -> {
                        f.setName(ObjectHelper.getCamelFieldName(f.getName())); 
                        return f;
                      })
                      .collect(Collectors.toList());

        Optional<DocumentData> docFound = docRep.findById(docId);
        
        if (!docFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return docDbSvc.findDocumentDataWithFilters(docFound.get(),filledFilters, page, size);
    }
}
