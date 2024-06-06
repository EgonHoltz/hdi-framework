package pt.hdi.loggerservice.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import pt.hdi.loggerservice.utils.ApplicationEnums.DATA_STATUS;
import pt.hdi.loggerservice.utils.ApplicationEnums.FLOW_DIRECTION;
import pt.hdi.loggerservice.utils.ApplicationEnums.TECHNOLOGY;

@Document(collection = "auditLogger")
@TypeAlias("AuditLogger")
public class AuditLogger {
    @Id
    protected String id;
    protected String configurationId;
    @DBRef
    protected Configuration configuration;
    Date creationDate;
    protected FLOW_DIRECTION flowDirection;
    protected TECHNOLOGY technonology;
    protected DATA_STATUS dataStatus;
    
    public AuditLogger() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public FLOW_DIRECTION getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(FLOW_DIRECTION flowDirection) {
        this.flowDirection = flowDirection;
    }

    public TECHNOLOGY getTechnonology() {
        return technonology;
    }

    public void setTechnonology(TECHNOLOGY technonology) {
        this.technonology = technonology;
    }

    public DATA_STATUS getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DATA_STATUS dataStatus) {
        this.dataStatus = dataStatus;
    }

    @Override
    public String toString() {
        return "AuditLogger [id=" + id + ", configurationId=" + configurationId + ", configuration=" + configuration
                + ", creationDate=" + creationDate + ", flowDirection=" + flowDirection + ", technonology="
                + technonology + ", dataStatus=" + dataStatus + "]";
    }


}
