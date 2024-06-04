package pt.hdi.restservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.restservice.Utils.ApplicationEnums.SEND_SFTP_STATUS;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.FileAuditLogger;
import pt.hdi.restservice.repository.AuditLoggerRepository;
import pt.hdi.restservice.repository.ConfigurationRepository;

@Service
public class AuditLoggerService {
    
    @Autowired
    private AuditLoggerRepository auditLogRep;

    @Autowired
    private ConfigurationRepository confRep;


    public List<FileAuditLogger> getAllFileAuditLogger(){
        return auditLogRep.findAll();
    }

    public FileAuditLogger addNewFileAuditLogger(FileAuditLogger fileAuditLogger){
        if (fileAuditLogger.getConfiguration() == null){
            Optional<Configuration> conf = confRep.findById(fileAuditLogger.getConfigurationId());
            fileAuditLogger.setSftpStatus(SEND_SFTP_STATUS.FILE_GENERATED);
            if (conf.isPresent()){
                fileAuditLogger.setConfiguration(conf.get());
            } else {
                return null;
            }
        }
        FileAuditLogger createdLogger = auditLogRep.save(fileAuditLogger);
        return createdLogger;
    }

    public List<FileAuditLogger> getAllFileAuditLoggerByConfiguration(Configuration configuration){
        return auditLogRep.findByConfigurationId(configuration.getId());
    }

    public List<FileAuditLogger> getAllFilesOnFileGenerateStatus(){        
        return auditLogRep.findBySftpStatus(SEND_SFTP_STATUS.FILE_GENERATED);
    }

    public void moveStatus(FileAuditLogger audit, SEND_SFTP_STATUS newStatus) {
        audit.setSftpStatus(newStatus);
        if (SEND_SFTP_STATUS.SENT.equals(newStatus)){
            audit.setFileSentDate(new Date());
        }
        auditLogRep.save(audit);
    }

    public FileAuditLogger getAuditById(String auditId) {
        Optional<FileAuditLogger> audit = auditLogRep.findById(auditId);
        if (audit.isPresent()){
            return audit.get();
        }
        return null;
    }

    public FileAuditLogger getAuditByFileName(String minioLink){
        return auditLogRep.findByMinioLink(minioLink);
    }

}
