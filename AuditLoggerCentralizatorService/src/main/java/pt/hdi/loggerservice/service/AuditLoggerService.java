package pt.hdi.loggerservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.loggerservice.model.AuditLogger;
import pt.hdi.loggerservice.model.Configuration;
import pt.hdi.loggerservice.model.FileAuditLogger;
import pt.hdi.loggerservice.repository.AuditLoggerRepository;
import pt.hdi.loggerservice.repository.ConfigurationRepository;
import pt.hdi.loggerservice.utils.ApplicationEnums.FLOW_DIRECTION;
import pt.hdi.loggerservice.utils.ApplicationEnums.SEND_SFTP_STATUS;
import pt.hdi.loggerservice.utils.ApplicationEnums.TECHNOLOGY;

@Service
public class AuditLoggerService {

    @Autowired
    private AuditLoggerRepository auditLogRep;

    @Autowired
    private ConfigurationRepository confRep;

    public List<AuditLogger> getAllFileAuditLogger(){
        return auditLogRep.findAll();
    }

    public void addNewAuditLogger(AuditLogger audit){
        if (audit.getConfiguration() == null){
            Optional<Configuration> conf = confRep.findById(audit.getConfigurationId());
            if (conf.isPresent()){
                audit.setConfiguration(conf.get());
            }
        }
        auditLogRep.save(audit);
    }

    public FileAuditLogger addNewFileAuditLogger(FileAuditLogger fileAuditLogger){
        if (fileAuditLogger.getConfiguration() == null){
            Optional<Configuration> conf = confRep.findById(fileAuditLogger.getConfigurationId());
            fileAuditLogger.setSftpStatus(SEND_SFTP_STATUS.FILE_GENERATED);
            fileAuditLogger.setFlowDirection(FLOW_DIRECTION.SEND);
            fileAuditLogger.setTechnonology(TECHNOLOGY.SFTP);
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
        List<AuditLogger> logs = auditLogRep.findByConfigurationId(configuration.getId());

        return logs.stream()
                .filter(l -> l instanceof FileAuditLogger)
                .map(l -> (FileAuditLogger) l)
                .collect(Collectors.toList());
    }

    public List<FileAuditLogger> getAllFilesOnFileGenerateStatus(){
        return auditLogRep.findFileAuditLoggerBySftpStatus(SEND_SFTP_STATUS.FILE_GENERATED);
    }

    public void moveStatus(FileAuditLogger audit, SEND_SFTP_STATUS newStatus) {
        audit.setSftpStatus(newStatus);
        if (SEND_SFTP_STATUS.SENT.equals(newStatus)){
            audit.setFileSentDate(new Date());
        }
        auditLogRep.save(audit);
    }

    public FileAuditLogger getAuditById(String auditId) {
        Optional<AuditLogger> audit = auditLogRep.findById(auditId);
        if (audit.isPresent() && audit.get() instanceof FileAuditLogger){
            return (FileAuditLogger) audit.get();
        }
        return null;
    }

    public FileAuditLogger getAuditByFileName(String minioLink){
        return auditLogRep.findFileAuditLoggerByMinioLink(minioLink);
    }
}
