package pt.hdi.sftpservice.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class Structure {
    String fieldName;
    String type;
    Boolean mandatory;
    String regExp;
    String linkedDocument;
    
    public Structure() {
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

    public boolean isMandatory() {
        if (mandatory == null){
            return false;
        }
        return mandatory.booleanValue();
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
    public String toString() {
        return "Structure [fieldName=" + fieldName + ", type=" + type + ", mandatory=" + mandatory + ", regExp="
                + regExp + ", linkedDocument=" + linkedDocument + "]";
    }

    
}
