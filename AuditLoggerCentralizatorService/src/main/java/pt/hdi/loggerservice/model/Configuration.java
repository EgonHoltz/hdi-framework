package pt.hdi.loggerservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
	
	@Id
	private String id;

	@DBRef
	private DocumentData documentData;

	@DBRef
	private Application application;

	private List<MQConfig> mqConfig;
	private List<SFTPConfig> sftpConfig;
	private List<GRPCConfig> grpcConfig;
	private SftpFileSchedulerConfig sftpSchedulerConfig;
	
	@LastModifiedDate
	private Date lastModificationDate;	
	@CreatedDate
	private Date createDate;
	
	public Configuration() {
		super();
	}
	public Configuration(DocumentData documentData, Application application) {
		super();
		this.documentData = documentData;
		this.application = application;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getLastModificationDate() {
		return lastModificationDate;
	}
	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
	public List<MQConfig> getMqConfig() {
		return this.mqConfig;
	}
	public void setMqConfig(List<MQConfig> mqConfig) {
		this.mqConfig = mqConfig;
	}
	public void addMqConfig(MQConfig mqConfig) {
		if (this.mqConfig == null) {
			this.mqConfig = new ArrayList<MQConfig>();
		}
		this.mqConfig.add(mqConfig);
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
	public DocumentData getDocumentData() {
		return documentData;
	}
	public void setDocumentData(DocumentData documentData) {
		this.documentData = documentData;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}	
	public SftpFileSchedulerConfig getSftpSchedulerConfig() {
		return sftpSchedulerConfig;
	}
	public void setSftpSchedulerConfig(SftpFileSchedulerConfig sftpSchedulerConfig) {
		this.sftpSchedulerConfig = sftpSchedulerConfig;
	}
	@Override
	public String toString() {
		return "Configuration [documentData=" + documentData + ", application=" + application + ", mqConfig=" + mqConfig
				+ ", sftpConfig=" + sftpConfig + ", grpcConfig=" + grpcConfig + ", sftpSchedulerConfig="
				+ sftpSchedulerConfig + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate
				+ "]";
	}

}
