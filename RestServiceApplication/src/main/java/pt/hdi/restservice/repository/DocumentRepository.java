package pt.hdi.restservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.hdi.restservice.model.DocumentData;

public interface DocumentRepository extends MongoRepository<DocumentData, String> {
    
}
