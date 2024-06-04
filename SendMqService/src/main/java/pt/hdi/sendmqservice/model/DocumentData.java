package pt.hdi.sendmqservice.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DocumentData{
    private String id;
    private String documentName;
    private String documentShortDetail;
    private String owner;
    private Boolean containsSensitiveData;
    private Boolean dataToHold;
    private String approvedBy;
    private String observation;   
    private String modelKey;
	private Date lastModificationDate;
    private Date createDate;

    private List<Structure> structures;


    public DocumentData() {
    }

    public DocumentData(DocumentData doc) {
        this.documentName = doc.getDocumentName();
        this.documentShortDetail = doc.getDocumentShortDetail();
        this.owner = doc.getOwner();
        this.containsSensitiveData = doc.getContainsSensitiveData();
        this.dataToHold = doc.getDataToHold();
        this.approvedBy = doc.getApprovedBy();
        this.observation = doc.getObservation();
        this.structures = doc.getStructures();
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

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public void addStructure(Structure structure){
        if (this.structures == null){
            this.structures = new ArrayList<>();
        }
        this.structures.add(structure);
    }

    public void removeStructureById(int id){
        this.structures.remove(id);
    }

    @Override
    public String toString() {
        return "Document [id=" + id + ", documentName=" + documentName + ", documentShortDetail="+ documentShortDetail + ", owner=" + owner 
                + ", containsSensitiveData=" + containsSensitiveData + ", dataToHold=" + dataToHold + ", approvedBy=" + approvedBy 
                + ", Observation=" + observation + ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
    }

    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
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
        DocumentData other = (DocumentData) obj;
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