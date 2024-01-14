package pt.hdi.restservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

	private String documentName;
	private List<MQConfig> mqConfig;
	private List<SFTPConfig> sftpConfig;
	
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
	public String getDocumentName() {
		return documentData.getDocumentName();
	}
	
	public List<SFTPConfig> getSftpConfig() {
		return sftpConfig;
	}
	public void removeAllSftpConfig() {
		this.sftpConfig.clear();
	}
	public void addMqConfig(SFTPConfig sftpConfig) {
		if (this.sftpConfig == null) {
			this.sftpConfig = new ArrayList<SFTPConfig>();
		}
		this.sftpConfig.add(sftpConfig);	
	}

	public List<MQConfig> getMqConfig() {
		return mqConfig;
	}
	public void removeAllMqConfig() {
		this.mqConfig.clear();
	}
	public void addMqConfig(MQConfig mqConfig) {
		if (this.mqConfig == null) {
			this.mqConfig = new ArrayList<MQConfig>();
		}
		this.mqConfig.add(mqConfig);
	}
	public boolean removeSftpConfig(SFTPConfig sftpConfig) {
		return this.sftpConfig.removeIf(s -> s.getSftpFileName().equals(sftpConfig.getSftpFileName()));
	}
	public Optional<SFTPConfig> getFirstSendSftpConfig() {
		return this.sftpConfig.stream().filter(s -> s.getDirection().equals("send")).findFirst();
	}
	public Optional<SFTPConfig> getFirstReceiveSftpConfig() {
		return this.sftpConfig.stream().filter(s -> s.getDirection().equals("recv")).findFirst();
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
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", lastModificationDate=" + lastModificationDate + ", createDate="
				+ createDate + ", documentName=" + documentName + ", mqConfig=" + mqConfig + ", sftpConfig="
				+ sftpConfig + "]";
	}

}
