package pt.hdi.restservice.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


public class Document{
    String id;
    String documentName;
    String documentShortDetail;
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
        this.documentShortDetail = doc.getDocumentShortDetail();
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
    public String getDocumentShortDetail() {
        return documentShortDetail;
    }

    public void setDocumentShortDetail(String documentShortDetail) {
        this.documentShortDetail = documentShortDetail;
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

    @Override
    public String toString() {
        return "Document [id=" + id + ", documentName=" + documentName + ", documentShortDetail="+ documentShortDetail + ", owner=" + owner 
                + ", containsSensitiveData=" + containsSensitiveData + ", dataToHold=" + dataToHold + ", approvedBy=" + approvedBy 
                + ", Observation=" + observation + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
        result = prime * result + ((documentShortDetail == null) ? 0 : documentShortDetail.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((containsSensitiveData == null) ? 0 : containsSensitiveData.hashCode());
        result = prime * result + ((dataToHold == null) ? 0 : dataToHold.hashCode());
        result = prime * result + ((approvedBy == null) ? 0 : approvedBy.hashCode());
        result = prime * result + ((observation == null) ? 0 : observation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        if (documentName == null) {
            if (other.documentName != null)
                return false;
        } else if (!documentName.equals(other.documentName))
            return false;
        if (documentShortDetail == null) {
            if (other.documentShortDetail != null)
                return false;
        } else if (!documentShortDetail.equals(other.documentShortDetail))
            return false;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        if (containsSensitiveData == null) {
            if (other.containsSensitiveData != null)
                return false;
        } else if (!containsSensitiveData.equals(other.containsSensitiveData))
            return false;
        if (dataToHold == null) {
            if (other.dataToHold != null)
                return false;
        } else if (!dataToHold.equals(other.dataToHold))
            return false;
        if (approvedBy == null) {
            if (other.approvedBy != null)
                return false;
        } else if (!approvedBy.equals(other.approvedBy))
            return false;
        if (observation == null) {
            if (other.observation != null)
                return false;
        } else if (!observation.equals(other.observation))
            return false;
        return true;
    }
    

}