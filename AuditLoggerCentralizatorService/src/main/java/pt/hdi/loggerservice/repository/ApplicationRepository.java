package pt.hdi.loggerservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.loggerservice.model.Application;

@Repository
public interface ApplicationRepository extends MongoRepository<Application,String> {
    
}
