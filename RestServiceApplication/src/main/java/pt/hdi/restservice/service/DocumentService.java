package pt.hdi.restservice.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.Utils.ApplicationEnums.DOCUMENT_STATUS;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.Structure;
import pt.hdi.restservice.repository.DocumentRepository;

@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository docRep;

    @Autowired
    private DataManagementService dtMgtSvc;


    private boolean hasRecordedStructureAgainstDb(List<Structure> structure, List<String> dbFields){

        for (Structure s : structure) {
            String fieldName = ObjectHelper.getCamelFieldName(s.getFieldName());
            if (dbFields.contains(fieldName)) {
               continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public ResponseEntity getDocumentStatusOnDb(DocumentData documentData){
        
        String collectionName = documentData.getDocumentName().replaceAll("\\s", "").toLowerCase();
        String modelKey = documentData.getModelKey();
        
        //Verify if Collection exists
        ResponseEntity resCollExists = dtMgtSvc.getCollectionExists(collectionName);
        if (resCollExists.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return new ResponseEntity(DOCUMENT_STATUS.INEXISTS,HttpStatus.OK);
        } else if (resCollExists.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        //Get collection structure and verify if match with configured structure
        List<String> collStt = new ArrayList<>();
        ResponseEntity resCollStt = dtMgtSvc.getCollectionFields(collectionName, modelKey);
        if (!resCollStt.getStatusCode().equals(HttpStatus.OK)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            collStt = (List)resCollStt.getBody();
        }
        
        if(!hasRecordedStructureAgainstDb(documentData.getStructures(),
        collStt)){
            return new ResponseEntity(DOCUMENT_STATUS.FIELDS_OUTDATED,HttpStatus.OK);
        }

        // No need to have changes. All updated        
        return new ResponseEntity(DOCUMENT_STATUS.ALL_UPDATED,HttpStatus.OK);
    }
    
    public boolean editStructureField(String documentId, Structure updatedStructure) {
        DocumentData document = docRep.findById(documentId).orElse(null);
        String fieldName = updatedStructure.getFieldName();

        if (document != null) {
            List<Structure> structures = document.getStructures();

            for (int i = 0; i < structures.size(); i++) {
                Structure existingStructure = structures.get(i);
                if (existingStructure.getFieldName().equals(fieldName)) {
                    document.removeStructureById(i);
                    document.addStructure(updatedStructure);                    
                    docRep.save(document);
                    return true; 
                }
            }
        }
        return false;
    }

    public boolean addStructure(String documentId, Structure updatedStructure) {
        DocumentData document = docRep.findById(documentId).orElse(null);

        if (document != null) {
            document.addStructure(updatedStructure);
            docRep.save(document);
            return true;
        }
        return false;
    }
    
    public ResponseEntity generateDocumentOnDb(DocumentData documentData) {        
        // to receive the ENUM to know what needs to be done
        ResponseEntity resDocStts = getDocumentStatusOnDb(documentData);
        if (!resDocStts.getStatusCode().equals(HttpStatus.OK)){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        DOCUMENT_STATUS docStts = (DOCUMENT_STATUS) resDocStts.getBody();
        String collectionName = documentData.getDocumentName().replaceAll("\\s", "").toLowerCase(); // Remove spaces for collection name
        switch (docStts) {
            case INEXISTS:
                ResponseEntity resCreateDoc = dtMgtSvc.createCollection(documentData);
                if (!resCreateDoc.getStatusCode().equals(HttpStatus.OK)){
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                } else {
                    String generatedId = (String) resCreateDoc.getBody();
                    documentData.setModelKey(generatedId);
                    docRep.save(documentData);
                    return new ResponseEntity(HttpStatus.OK);
                }
            case FIELDS_OUTDATED:
                List<String> dbFields = new ArrayList<>();
                ResponseEntity resCollStt = dtMgtSvc.getCollectionFields(collectionName, documentData.getModelKey());
                if (!resCollStt.getStatusCode().equals(HttpStatus.OK)){
                    return new ResponseEntity(DOCUMENT_STATUS.INEXISTS,HttpStatus.BAD_REQUEST);
                } else {
                    dbFields = (List)resCollStt.getBody();
                }
                List<Structure> structures = documentData.getStructures();
                boolean result = true;
                for (Structure s : structures) {
                    String fieldName = ObjectHelper.getCamelFieldName(s.getFieldName());
                    if (dbFields.contains(fieldName)) {
                        continue;
                    } else {
                        ResponseEntity resEditDoc = dtMgtSvc.editCollection(collectionName, documentData.getModelKey(), s);
                        if (!resEditDoc.getStatusCode().equals(HttpStatus.OK)){
                            result &= false;
                        }
                    }
                }
                if (result){
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                
            case ALL_UPDATED:
                return new ResponseEntity(HttpStatus.OK);
                default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    // Implement this method to map Structure.type to appropriate Java type
    private Object getValueForType(String type) {
        // Implement type mapping based on your requirements
        // For example:
        if ("string".equalsIgnoreCase(type)) {
            return ""; // Default value for String type
        } else if ("integer".equalsIgnoreCase(type)) {
            return 0; // Default value for Integer type
        } else if ("boolean".equalsIgnoreCase(type)) {
            return false; // Default value for Boolean type
        }
        // Add more type mappings as needed
        return null; // Default value
    }

}
