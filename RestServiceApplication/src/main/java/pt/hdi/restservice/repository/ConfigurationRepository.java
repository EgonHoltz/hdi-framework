package pt.hdi.restservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;

import java.util.List;




@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String>{
	@Query("{'documentData': ?0, 'application': ?1}")
	Configuration findByDocumentApplication(String documentDataId, String applicationId);

}
