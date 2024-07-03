package pt.hdi.filegenerator.model;

import java.util.Date;

public class FileAuditLogger {
    String configurationId;
    String fileName;
    String minioLink;
    Date creationDate;
    Long lineQuantity;

    public FileAuditLogger() {
        super();
    }

    public FileAuditLogger(String configurationId, String fileName , String minioLink, Date creationDate,
            Long lineQuantity) {
        this.fileName = fileName;
        this.configurationId = configurationId;
        this.creationDate = creationDate;
        this.minioLink = minioLink;
        this.lineQuantity = lineQuantity;
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

    @Override
    public String toString() {
        return "FileAuditLogger [fileName=" + fileName + ", configurationId=" + configurationId + ", creationDate="
                + creationDate + ", minioLink=" + minioLink + ", lineQuantity=" + lineQuantity + "]";
    }

}
