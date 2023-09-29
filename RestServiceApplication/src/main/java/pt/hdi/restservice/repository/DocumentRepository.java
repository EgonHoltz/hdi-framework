package pt.hdi.restservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.hdi.restservice.model.Document;

public interface DocumentRepository extends MongoRepository<Document, String> {
    
}
