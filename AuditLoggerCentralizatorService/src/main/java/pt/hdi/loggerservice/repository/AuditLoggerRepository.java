package pt.hdi.loggerservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import pt.hdi.loggerservice.model.AuditLogger;
import pt.hdi.loggerservice.model.FileAuditLogger;
import pt.hdi.loggerservice.utils.ApplicationEnums.SEND_SFTP_STATUS;

@Repository
public interface AuditLoggerRepository extends MongoRepository<AuditLogger,String>{
    List<AuditLogger> findByConfigurationId(String configurationId);
    @Query("{ 'sftpStatus' : ?0 }")
    List<FileAuditLogger> findFileAuditLoggerBySftpStatus(SEND_SFTP_STATUS sftpStatus);
    @Query("{ 'minioLink' : ?0 }")
    FileAuditLogger findFileAuditLoggerByMinioLink(String minioLink);
}
