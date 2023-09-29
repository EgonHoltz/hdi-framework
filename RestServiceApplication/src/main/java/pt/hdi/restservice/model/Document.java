package pt.hdi.restservice.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


public class Document{
    String id;
    String documentName;
    String owner;
    Boolean containsSensitiveData;
    Boolean dataToHold;
    String approvedBy;
    String observation;

    @LastModifiedDate
	private Date lastModificationDate;
	
	@CreatedDate
	private Date createDate;

    public Document() {
    }

    public Document(Document doc) {
        this.documentName = doc.getDocumentName();
        this.owner = doc.getOwner();
        this.containsSensitiveData = doc.getContainsSensitiveData();
        this.dataToHold = doc.getDataToHold();
        this.approvedBy = doc.getApprovedBy();
        this.observation = doc.getObservation();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public String toString() {
        return "Document [id=" + id + ", documentName=" + documentName + ", owner=" + owner + ", containsSensitiveData="
                + containsSensitiveData + ", dataToHold=" + dataToHold + ", approvedBy=" + approvedBy + ", Observation="
                + observation + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getContainsSensitiveData() {
        return containsSensitiveData;
    }

    public void setContainsSensitiveData(Boolean containsSensitiveData) {
        this.containsSensitiveData = containsSensitiveData;
    }

    public Boolean getDataToHold() {
        return dataToHold;
    }

    public void setDataToHold(Boolean dataToHold) {
        this.dataToHold = dataToHold;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getObservation() {
        return this.observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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

    

}