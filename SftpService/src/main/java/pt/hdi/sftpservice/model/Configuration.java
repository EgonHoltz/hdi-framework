package pt.hdi.sftpservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
	
	private String id;
	private String documentDataId;
	private String applicationId;
	private String documentDataName; 

	private List<SFTPConfig> sftpConfig;
		
	public Configuration() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<SFTPConfig> getSftpConfig() {
		return sftpConfig;
	}

	public void setSftpConfig(List<SFTPConfig> sftpConfig) {
		this.sftpConfig = sftpConfig;
	}
	public String getDocumentDataId() {
		return documentDataId;
	}
	public void setDocumentDataId(String documentDataId) {
		this.documentDataId = documentDataId;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getDocumentDataName() {
		return documentDataName;
	}
	public void setDocumentDataName(String documentDataName) {
		this.documentDataName = documentDataName;
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", documentDataId=" + documentDataId + ", applicationId=" + applicationId
				+ ", documentDataName=" + documentDataName + ", sftpConfig=" + sftpConfig + "]";
	}

}
