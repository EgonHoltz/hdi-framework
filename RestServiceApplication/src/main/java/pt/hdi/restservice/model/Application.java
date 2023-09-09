package pt.hdi.restservice.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class Application {

    String id;
    String appName;
    String appAbrv;
    String functionalId;
    String Owner;
    Long recvData;
    Long sentData;
    BigDecimal errorsPercentage;
    String observations;
    Boolean highAvaiability;
    
    @CreatedDate
    Date creationDate;
    String createdBy;

    @LastModifiedDate
    Date lastModDate;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getAppAbrv() {
        return appAbrv;
    }
    public void setAppAbrv(String appAbrv) {
        this.appAbrv = appAbrv;
    }
    public String getFunctionalId() {
        return functionalId;
    }
    public void setFunctionalId(String functionalId) {
        this.functionalId = functionalId;
    }
    public String getOwner() {
        return Owner;
    }
    public void setOwner(String owner) {
        Owner = owner;
    }
    public Long getRecvData() {
        return recvData;
    }
    public void setRecvData(Long recvData) {
        this.recvData = recvData;
    }
    public Long getSentData() {
        return sentData;
    }
    public void setSentData(Long sentData) {
        this.sentData = sentData;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Date getLastModDate() {
        return lastModDate;
    }
    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }
    public Boolean getHighAvaiability() {
        return highAvaiability;
    }
    public void setHighAvaiability(Boolean highAvaiability) {
        this.highAvaiability = highAvaiability;
    }
    public BigDecimal getErrorsPercentage() {
        return errorsPercentage;
    }
    public void setErrorsPercentage(BigDecimal errorsPercentage) {
        this.errorsPercentage = errorsPercentage;
    }
    public String getObservations() {
        return observations;
    }
    public void setObservations(String observations) {
        this.observations = observations;
    }


    
}
