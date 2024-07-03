package pt.hdi.restservice.model;

import java.util.Date;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import pt.hdi.restservice.Utils.ApplicationEnums.SEND_SFTP_STATUS;

@Document(collection = "auditLogger")
@TypeAlias("FileAuditLogger")
public class FileAuditLogger extends AuditLogger {

    private String fileName;
    private String minioLink;
    private Long lineQuantity;
    private Date fileSentDate;
    private SEND_SFTP_STATUS sftpStatus;
    
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
    public Date getFileSentDate() {
        return fileSentDate;
    }
    public void setFileSentDate(Date fileSentDate) {
        this.fileSentDate = fileSentDate;
    }
    public SEND_SFTP_STATUS getSftpStatus() {
        return sftpStatus;
    }
    public void setSftpStatus(SEND_SFTP_STATUS sftpStatus) {
        this.sftpStatus = sftpStatus;
    }
    @Override
    public String toString() {
        return "FileAuditLogger [id=" + id + ", configurationId=" + configurationId + ", configuration=" + configuration
                + ", fileName=" + fileName + ", minioLink=" + minioLink + ", creationDate=" + creationDate
                + ", lineQuantity=" + lineQuantity + ", fileSentDate=" + fileSentDate + ", sftpStatus=" + sftpStatus
                + "]";
    }
    
}
