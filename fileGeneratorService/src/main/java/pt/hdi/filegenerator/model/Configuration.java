package pt.hdi.filegenerator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties.Application;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
	
	private String id;

	private String documentDataId;
	private String applicationId;
	private String documentDataName;
	private String applicationAbreviation;
	private List<SFTPConfig> sftpConfig;
	private SftpFileSchedulerConfig sftpSchedulerConfig;
		
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
		return this.sftpConfig;
	}
	public void setSftpConfig(List<SFTPConfig> sftpConfig) {
		this.sftpConfig = sftpConfig;
	}
	public void addSftpConfig(SFTPConfig sftpConfig) {
		if (this.sftpConfig == null) {
			this.sftpConfig = new ArrayList<SFTPConfig>();
		}
		this.sftpConfig.add(sftpConfig);	
	}
	public SftpFileSchedulerConfig getSftpSchedulerConfig() {
		return sftpSchedulerConfig;
	}
	public void setSftpSchedulerConfig(SftpFileSchedulerConfig sftpSchedulerConfig) {
		this.sftpSchedulerConfig = sftpSchedulerConfig;
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
	public String getApplicationAbreviation() {
		return applicationAbreviation;
	}
	public void setApplicationAbreviation(String applicationAbreviation) {
		this.applicationAbreviation = applicationAbreviation;
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", documentDataId=" + documentDataId + ", applicationId=" + applicationId
				+ ", documentDataName=" + documentDataName + ", applicationAbreviation=" + applicationAbreviation
				+ ", sftpConfig=" + sftpConfig + ", sftpSchedulerConfig=" + sftpSchedulerConfig + "]";
	}

}
