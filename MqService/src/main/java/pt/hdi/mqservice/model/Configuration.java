package pt.hdi.mqservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	
	public Configuration() {
		super();
	}
	public Configuration(String documentName, List<MQConfig> mqConfig) {
		super();
		this.documentName = documentName;
		this.mqConfig = mqConfig;
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
				+ createDate + ", documentName=" + documentName + ", mqConfig=" + mqConfig + "]";
	}

}
