package pt.hdi.grpcservice.model;

import java.util.Date;

import pt.hdi.grpcservice.utils.ApplicationEnums.DATA_STATUS;
import pt.hdi.grpcservice.utils.ApplicationEnums.FLOW_DIRECTION;
import pt.hdi.grpcservice.utils.ApplicationEnums.TECHNOLOGY;

public class AuditLogger {
    private String configurationId;
    private Configuration configuration;
    private Date creationDate;
    private FLOW_DIRECTION flowDirection;
    private TECHNOLOGY technonology;
    private DATA_STATUS dataStatus;
    
    public AuditLogger() {
    }

    public AuditLogger(String configurationId, Configuration configuration, Date creationDate,
            FLOW_DIRECTION flowDirection, TECHNOLOGY technonology, DATA_STATUS dataStatus) {
        this.configurationId = configurationId;
        this.configuration = configuration;
        this.creationDate = creationDate;
        this.flowDirection = flowDirection;
        this.technonology = technonology;
        this.dataStatus = dataStatus;
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
        return "AuditLogger [configurationId=" + configurationId + ", configuration=" + configuration
                + ", creationDate=" + creationDate + ", flowDirection=" + flowDirection + ", technonology="
                + technonology + ", dataStatus=" + dataStatus + "]";
    }

}
