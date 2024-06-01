package pt.hdi.restservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pt.hdi.restservice.model.FileAuditLogger;

@Repository
public interface AuditLoggerRepository extends MongoRepository<FileAuditLogger,String>{
    List<FileAuditLogger> findByConfigurationId(String configurationId);
}
