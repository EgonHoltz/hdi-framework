package pt.hdi.grpcservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.grpcservice.model.Configuration;


@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String>{
	Configuration findByDocumentName(String documentName);

}
