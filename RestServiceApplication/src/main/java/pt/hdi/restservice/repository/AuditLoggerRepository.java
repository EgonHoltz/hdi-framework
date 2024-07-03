package pt.hdi.restservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pt.hdi.restservice.Utils.ApplicationEnums.SEND_SFTP_STATUS;
import pt.hdi.restservice.model.AuditLogger;

@Repository
public interface AuditLoggerRepository extends MongoRepository<AuditLogger,String>{
    List<AuditLogger> findByConfigurationId(String configurationId);
    @Query("{ '_class' : 'pt.hdi.restservice.model.FileAuditLogger', 'sftpStatus' : ?0 }")
    List<AuditLogger> findBySftpStatus(SEND_SFTP_STATUS sftpStatus);
    @Query("{ '_class' : 'pt.hdi.restservice.model.FileAuditLogger', 'minioLink' : ?0 }")
    AuditLogger findByMinioLink(String minioLink);
}
