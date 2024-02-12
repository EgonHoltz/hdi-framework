package pt.hdi.mqservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import pt.hdi.mqservice.Utils.ApplicationEnums;


public class Configuration {
	
	private String id;
	private String documentDataId;
	private String applicationId;

	private List<MQConfig> mqConfig;
		
	public Configuration() {
		super();
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

}
