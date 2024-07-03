package pt.hdi.loggerservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pt.hdi.loggerservice.model.Configuration;

import java.util.List;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String>{
	@Query("{'documentData': ?0, 'application': ?1}")
	Configuration findByDocumentApplication(String documentDataId, String applicationId);
	List<Configuration> findConfigurationByMqConfigStarted(Boolean started);
	Configuration findByMqConfigMqName(String rqName);
	Configuration findBySftpConfigSftpFileName(String fileName);
	Configuration findByGrpcConfigClientId(String clientId);
	List<Configuration> findConfigurationsByApplication(String applicationId);

}
