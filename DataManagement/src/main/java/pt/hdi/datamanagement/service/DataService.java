package pt.hdi.datamanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;

import pt.hdi.datamanagement.model.Structure;
import pt.hdi.datamanagement.utils.ObjectHelper;

@Service
public class DataService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<String> getCollectionFields(String collectionName, String modelKey) {
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


    public ResponseEntity doCollectionInsert(List<Structure> docStructure, String collectionName) {
        Document document = new Document();
        for (Structure structure : docStructure) {
            String fieldName = ObjectHelper.getCamelFieldName(structure.getFieldName());
            String type = structure.getType();
            document.append(fieldName, getValueForType(type)); // You may need to convert types or handle differently
        }
      
        // Generate collection name based on documentName
        mongoTemplate.createCollection(collectionName);
        InsertOneResult docInsert = mongoTemplate.getCollection(collectionName).insertOne(document);
        if (docInsert.getInsertedId() != null){
            String generatedId = docInsert.getInsertedId().asObjectId().getValue().toHexString();
            return new ResponseEntity(generatedId, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private Object getValueForType(String type) {
        if ("string".equalsIgnoreCase(type)) {
            return ""; // Default value for String type
        } else if ("integer".equalsIgnoreCase(type)) {
            return 0; // Default value for Integer type
        } else if ("boolean".equalsIgnoreCase(type)) {
            return false; // Default value for Boolean type
        }
        return null;
    }

    public <T> ResponseEntity<Object> addDummyAttribute(String collectionName, String modelKey, Structure structure) {
        // Find the object by ID
        try {
            T object = mongoTemplate.findById(new ObjectId(modelKey), (Class<T>) Object.class, collectionName);
            
            if (object != null) {
                String fieldName = ObjectHelper.getCamelFieldName(structure.getFieldName());
                String type = structure.getType();
                mongoTemplate.getCollection(collectionName).updateOne(new Document("_id", new ObjectId(modelKey)),
                        new Document("$set", new Document(fieldName, getValueForType(type))));
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Handle if the object is not found
                System.out.println("Object with ID " + modelKey + " not found in collection " + collectionName);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity verifyCollectionExists(String collectionName){
        if (mongoTemplate.getCollectionNames().contains((collectionName))){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
