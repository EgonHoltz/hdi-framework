package pt.hdi.restservice.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SoftDeleteTransition {
    
    @Id
    private String id;
    private String collectionId;
    private String dataId;
    private String user;

    @LastModifiedDate
	private Date lastModificationDate;
	
	@CreatedDate
	private Date createDate;
    
    public SoftDeleteTransition() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }  
    public String getCollectionId() {
        return collectionId;
    }
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "SoftDeleteTransition [collectionId=" + collectionId + ", dataId=" + dataId + ", user=" + user
                + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
    }  
}