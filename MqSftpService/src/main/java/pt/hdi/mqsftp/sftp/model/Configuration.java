package pt.hdi.mqsftp.sftp.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
	
	private String id;

	@LastModifiedDate
	private Date lastModificationDate;
	
	@CreatedDate
	private Date createDate;
	private String documentName;
	private List<MQConfig> mqConfig;
	private String sftpFileName;
	
	
	public Configuration() {
		super();
	}
	public Configuration(String documentName, List<MQConfig> mqConfig, String sftpFileName) {
		super();
		this.documentName = documentName;
		this.mqConfig = mqConfig;
		this.sftpFileName = sftpFileName;
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
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getSftpFileName() {
		return sftpFileName;
	}
	public void setSftpFileName(String sftpFileName) {
		this.sftpFileName = sftpFileName;
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
	
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", lastModificationDate=" + lastModificationDate + ", createDate="
				+ createDate + ", documentName=" + documentName + ", mqConfig=" + mqConfig + ", sftpFileName="
				+ sftpFileName + "]";
	}

}
