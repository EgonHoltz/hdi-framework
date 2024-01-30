package pt.hdi.restservice.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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

    private final MongoTemplate mongoTemplate;

    public DocumentService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private List<String> getCollectionFields(String collectionName, String modelKey) {
        MongoCollection<Document> document =  mongoTemplate.getCollection(collectionName);
        Query q = new Query();
        q.limit(1);
        Document doc = mongoTemplate.findOne(q, org.bson.Document.class, collectionName);
        // Get distinct field values from the collection
        List<String> fieldNames = new ArrayList<>();
        // Add field names to the set
        if (document != null) {
            for (String key : doc.keySet()) {
                if (key.equals("_id")){
                    continue;
                }
                fieldNames.add(key);
            }
        }
        return fieldNames;
    }

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

    public DOCUMENT_STATUS getDocumentStatusOnDb(DocumentData documentData){
        
        String collectionName = documentData.getDocumentName().replaceAll("\\s", "").toLowerCase();
        String modelKey = documentData.getModelKey();
        if (!mongoTemplate.getCollectionNames().contains((collectionName))){
            return DOCUMENT_STATUS.INEXISTS;
        }
        
        if(!hasRecordedStructureAgainstDb(documentData.getStructures(),
            getCollectionFields(collectionName, modelKey))){
            return DOCUMENT_STATUS.FIELDS_OUTDATED;
        }
        
        return DOCUMENT_STATUS.ALL_UPDATED;
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



    public boolean generateDocumentOnDb(DocumentData documentData) {
        Document document = new Document();
        // to receive the ENUM to know what needs to be done
        DOCUMENT_STATUS docStts = getDocumentStatusOnDb(documentData);
        String collectionName = documentData.getDocumentName().replaceAll("\\s", "").toLowerCase(); // Remove spaces for collection name
        switch (docStts) {
            case INEXISTS:
                for (Structure structure : documentData.getStructures()) {
                    String fieldName = ObjectHelper.getCamelFieldName(structure.getFieldName());
                    String type = structure.getType();
                    document.append(fieldName, getValueForType(type)); // You may need to convert types or handle differently
                }
        
                // Generate collection name based on documentName
                mongoTemplate.createCollection(collectionName);
                InsertOneResult docInsert = mongoTemplate.getCollection(collectionName).insertOne(document);
                if (docInsert.getInsertedId() != null){
                    String generatedId = docInsert.getInsertedId().asObjectId().getValue().toHexString();
                    documentData.setModelKey(generatedId);
                    docRep.save(documentData);
                    return true;
                } else {
                    return false;
                }        
            case FIELDS_OUTDATED:
                List<String> dbFields = getCollectionFields(collectionName, documentData.getModelKey());
                List<Structure> structures = documentData.getStructures();
                for (Structure s : structures) {
                    String fieldName = ObjectHelper.getCamelFieldName(s.getFieldName());
                    if (dbFields.contains(fieldName)) {
                    continue;
                    } else {
                        addDummyAttribute(documentData, s);
                    }
                }
                return true;

            case ALL_UPDATED:
                return true;
            default:
                return false;
        }

    }

    public <T> void addDummyAttribute(DocumentData docData, Structure structure) {
        String collectionName = docData.getDocumentName().replaceAll("\\s", "").toLowerCase();
        // Find the object by ID
        T object = mongoTemplate.findById(new ObjectId(docData.getModelKey()), (Class<T>) Object.class, collectionName);
        
        if (object != null) {
            String fieldName = ObjectHelper.getCamelFieldName(structure.getFieldName());
            String type = structure.getType();
            mongoTemplate.getCollection(collectionName).updateOne(new Document("_id", new ObjectId(docData.getModelKey())),
                    new Document("$set", new Document(fieldName, getValueForType(type))));
        } else {
            // Handle if the object is not found
            System.out.println("Object with ID " + docData.getModelKey() + " not found in collection " + collectionName);
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
