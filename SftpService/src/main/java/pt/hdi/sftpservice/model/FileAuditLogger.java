package pt.hdi.sftpservice.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import pt.hdi.sftpservice.utils.ApplicationEnums.SEND_SFTP_STATUS;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FileAuditLogger {
    String id;
    String configurationId;
    String fileName;
    String minioLink;
    Date creationDate;
    Date fileSentDate;
    Long lineQuantity;
    Configuration configuration;
    SEND_SFTP_STATUS sftpStatus;

    public FileAuditLogger() {
        super();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public SEND_SFTP_STATUS getSftpStatus() {
        return sftpStatus;
    }

    public void setSftpStatus(SEND_SFTP_STATUS sftpStatus) {
        this.sftpStatus = sftpStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMinioLink() {
        return minioLink;
    }

    public void setMinioLink(String minioLink) {
        this.minioLink = minioLink;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FileAuditLogger [id=" + id + ", configurationId=" + configurationId + ", fileName=" + fileName
                + ", minioLink=" + minioLink + ", creationDate=" + creationDate + ", fileSentDate=" + fileSentDate
                + ", lineQuantity=" + lineQuantity + ", configuration=" + configuration + ", sftpStatus=" + sftpStatus
                + "]";
    }


}
