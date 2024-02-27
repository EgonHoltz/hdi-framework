package pt.hdi.restservice.bean;

import java.util.List;

import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.MQConfig;

public class ConfigurationMQBean {
	private String id;
	private String documentDataId;
	private String applicationId;
	private String documentDataName; 

	private List<MQConfig> mqConfig;
		
	public ConfigurationMQBean() {
		super();
	}
	public ConfigurationMQBean(Configuration config) {
		this.id = config.getId();
		this.documentDataId = config.getDocumentData().getId();
		this.applicationId = config.getApplication().getId();
		this.mqConfig = config.getMqConfig();
		this.documentDataName = config.getDocumentData().getDocumentName();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<MQConfig> getMqConfig() {
		return mqConfig;
	}

	public void setMqConfig(List<MQConfig> mqConfig) {
		this.mqConfig = mqConfig;
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
	@Override
	public String toString() {
		return "ConfigurationMQBean [id=" + id + ", documentDataId=" + documentDataId + ", applicationId="
				+ applicationId + ", mqConfig=" + mqConfig + "]";
	}
	public String getDocumentDataName() {
		return documentDataName;
	}
}
