package pt.hdi.mqservice.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class Structure {
    String fieldName;
    String type;
    Boolean mandatory;
    String regExp;
    String linkedDocument;

    @LastModifiedDate
	private Date lastModificationDate;
	@CreatedDate
	private Date createDate;

    
    public Structure(String fieldName, String type, Boolean mandatory, String regExp, String linkedDocument) {
        this.fieldName = fieldName;
        this.type = type;
        this.mandatory = mandatory;
        this.regExp = regExp;
        this.linkedDocument = linkedDocument;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getLinkedDocument() {
        return linkedDocument;
    }

    public void setLinkedDocument(String linkedDocument) {
        this.linkedDocument = linkedDocument;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((mandatory == null) ? 0 : mandatory.hashCode());
        result = prime * result + ((regExp == null) ? 0 : regExp.hashCode());
        result = prime * result + ((linkedDocument == null) ? 0 : linkedDocument.hashCode());
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
        Structure other = (Structure) obj;
        if (fieldName == null) {
            if (other.fieldName != null)
                return false;
        } else if (!fieldName.equals(other.fieldName))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (mandatory == null) {
            if (other.mandatory != null)
                return false;
        } else if (!mandatory.equals(other.mandatory))
            return false;
        if (regExp == null) {
            if (other.regExp != null)
                return false;
        } else if (!regExp.equals(other.regExp))
            return false;
        if (linkedDocument == null) {
            if (other.linkedDocument != null)
                return false;
        } else if (!linkedDocument.equals(other.linkedDocument))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Structure [fieldName=" + fieldName + ", type=" + type + ", mandatory=" + mandatory + ", regExp="
                + regExp + ", linkedDocument=" + linkedDocument + "]";
    }

    
}
