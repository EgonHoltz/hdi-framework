package pt.hdi.restservice.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.GRPCConfig;

@Document
public class ConfigurationGrpcBean {
	
	@Id
	private String id;

	private String documentDataId;
	private String applicationId;
	private String documentDataName; 

	private List<GRPCConfig> grpcConfig;
	
	public ConfigurationGrpcBean() {
		super();
	}
	public ConfigurationGrpcBean(Configuration config) {
		this.id = config.getId();
		this.documentDataId = config.getDocumentData().getId();
		this.applicationId = config.getApplication().getId();
		this.grpcConfig = config.getGrpcConfig();
		this.documentDataName = config.getDocumentData().getDocumentName();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<GRPCConfig> getGrpcConfig() {
		return grpcConfig;
	}
	public void setGrpcConfig(List<GRPCConfig> grpcConfig) {
		this.grpcConfig = grpcConfig;
	}
	public void addGrpcConfig(GRPCConfig grpcConfig){
		if (this.grpcConfig == null) {
			this.grpcConfig = new ArrayList<>();
		}
		this.grpcConfig.add(grpcConfig);
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
		return "ConfigurationGrpcBean [id=" + id + ", documentDataId=" + documentDataId + ", applicationId="
				+ applicationId + ", documentDataName=" + documentDataName + ", grpcConfig=" + grpcConfig + "]";
	}

}
