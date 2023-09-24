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
    String owner;
    Long recvData;
    Long sentData;
    BigDecimal errorsPercentage;
    String observations;
    Boolean highAvailability;
    
    @CreatedDate
    Date creationDate;
    String createdBy;

    @LastModifiedDate
    Date lastModDate;

    public Application() {
    }
    public Application(Application app) {
        this.appName = app.getAppName();
        this.appAbrv = app.getAppAbrv();
        this.functionalId = app.getFunctionalId();
        this.owner = app.getOwner();
        this.recvData = 0L;
        this.sentData = 0L;
        this.errorsPercentage = new BigDecimal(0);
        this.observations = app.getObservations();
        this.highAvailability = app.getHighAvailability();
    }
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
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
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
    public Boolean getHighAvailability() {
        return highAvailability;
    }
    public void setHighAvailability(Boolean highAvailability) {
        this.highAvailability = highAvailability;
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
