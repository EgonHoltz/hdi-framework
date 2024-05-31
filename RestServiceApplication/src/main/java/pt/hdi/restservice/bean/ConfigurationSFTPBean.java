package pt.hdi.restservice.bean;

import java.util.List;

import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.SFTPConfig;

public class ConfigurationSFTPBean {
	private String id;
	private String documentDataId;
	private String applicationId;
	private String documentDataName; 

	private List<SFTPConfig> sftpConfig;
		
	public ConfigurationSFTPBean() {
		super();
	}
	public ConfigurationSFTPBean(Configuration config) {
		this.id = config.getId();
		this.documentDataId = config.getDocumentData().getId();
		this.applicationId = config.getApplication().getId();
		this.sftpConfig = config.getSftpConfig();
		this.documentDataName = config.getDocumentData().getDocumentName();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setDocumentDataName(String documentDataName) {
		this.documentDataName = documentDataName;
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
	@Override
	public String toString() {
		return "ConfigurationSFTPBean [id=" + id + ", documentDataId=" + documentDataId + ", applicationId="
				+ applicationId + ", documentDataName=" + documentDataName + ", sftpConfig=" + sftpConfig + "]";
	}

}
