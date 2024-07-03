package pt.hdi.loggerservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.loggerservice.model.DocumentData;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentData, String> {
    
}
