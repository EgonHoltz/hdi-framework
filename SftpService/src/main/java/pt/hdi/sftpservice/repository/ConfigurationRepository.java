package pt.hdi.sftpservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.sftpservice.model.Configuration;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String>{
	Configuration findByDocumentName(String documentName);
	Configuration findByMqConfigMqName(String rqName);
	List<Configuration> findConfigurationByMqConfigStarted(Boolean started);

}
