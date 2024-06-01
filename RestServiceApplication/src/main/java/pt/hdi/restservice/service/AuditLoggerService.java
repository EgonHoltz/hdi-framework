package pt.hdi.restservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
