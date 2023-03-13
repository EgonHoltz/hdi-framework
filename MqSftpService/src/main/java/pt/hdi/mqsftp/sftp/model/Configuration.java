package pt.hdi.mqsftp.sftp.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
	
	private String id;
	private Date lastModification;
	private String documentName;
	private List<MQConfig> mqConfig;
	private String sftpFileName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getLastModification() {
		return lastModification;
	}
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
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
	public void addMqConfig(MQConfig mqConfig) {
		if (this.mqConfig == null) {
			this.mqConfig = new ArrayList<MQConfig>();
		}
		this.mqConfig.add(mqConfig);	
	}
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", lastModification=" + lastModification + ", documentName=" + documentName
				+ ", mqConfig=" + mqConfig + ", sftpFileName=" + sftpFileName + "]";
	}
	
	
	

}
