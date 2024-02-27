package pt.hdi.restservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.restservice.model.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application,String> {
    
}
