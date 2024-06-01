package pt.hdi.restservice.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

public class FileAuditLogger {
    @Id
    String id;
    String configurationId;
    @DBRef
    Configuration configuration;
    String fileName;
    String minioLink;
    Date creationDate;
    Long lineQuantity;
    public FileAuditLogger() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getConfigurationId() {
        return configurationId;
    }
    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }
    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getMinioLink() {
        return minioLink;
    }
    public void setMinioLink(String minioLink) {
        this.minioLink = minioLink;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Long getLineQuantity() {
        return lineQuantity;
    }
    public void setLineQuantity(Long lineQuantity) {
        this.lineQuantity = lineQuantity;
    }
    @Override
    public String toString() {
        return "FileAuditLogger [id=" + id + ", configurationId=" + configurationId + ", configuration=" + configuration
                + ", fileName=" + fileName + ", minioLink=" + minioLink + ", creationDate=" + creationDate
                + ", lineQuantity=" + lineQuantity + "]";
    }


    
}
