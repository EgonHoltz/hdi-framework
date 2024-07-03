package pt.hdi.restservice.bean;

import java.util.List;

import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.model.SftpFileSchedulerConfig;

public class ConfigurationSFTPSchedulerBean {
	private String id;
	private String documentDataId;
	private String applicationId;
	private String documentDataName;
	private String applicationAbreviation;
	private SftpFileSchedulerConfig sftpSchedulerConfig;

	private List<SFTPConfig> sftpConfig;
		
	public ConfigurationSFTPSchedulerBean() {
		super();
	}
	public ConfigurationSFTPSchedulerBean(Configuration config) {
		this.id = config.getId();
		this.documentDataId = config.getDocumentData().getId();
		this.applicationId = config.getApplication().getId();
		this.documentDataName = config.getDocumentData().getDocumentName();
		this.applicationAbreviation = config.getApplication().getAppAbrv();
		this.sftpConfig = config.getSftpConfig();
		this.sftpSchedulerConfig = config.getSftpSchedulerConfig();
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
		return "ConfigurationSFTPSchedulerBean [id=" + id + ", documentDataId=" + documentDataId + ", applicationId="
				+ applicationId + ", documentDataName=" + documentDataName + ", applicationAbreviation="
				+ applicationAbreviation + ", sftpSchedulerConfig=" + sftpSchedulerConfig + ", sftpConfig=" + sftpConfig
				+ "]";
	}
	

}
