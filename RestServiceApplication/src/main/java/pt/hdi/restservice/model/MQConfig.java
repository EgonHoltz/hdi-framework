package pt.hdi.restservice.model;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import pt.hdi.restservice.Utils.ApplicationEnums.FLOW_DIRECTION;

public class MQConfig {
	private String mqName;
	private FLOW_DIRECTION direction;
	private Boolean hasAck;
	private Boolean started;
	@LastModifiedDate
	private Date lastModificationDate;
	@CreatedDate
	private Date createDate;
	
	public MQConfig() {
		super();
	}

	public MQConfig(String mqName, FLOW_DIRECTION direction) {
		super();
		this.mqName = mqName;
		this.direction = direction;
	}
	
	public String getMqName() {
		return mqName;
	}
	public void setMqName(String mqName) {
		this.mqName = mqName;
	}
	public FLOW_DIRECTION getDirection() {
		return direction;
	}
	public void FLOW_DIRECTION(FLOW_DIRECTION direction) {
		this.direction = direction;
	}
	public Boolean getHasAck() {
		return hasAck;
	}
	public void setHasAck(Boolean hasAck) {
		this.hasAck = hasAck;
	}
	public Boolean hasStarted() {
		return this.started == null ? false : this.started;
	}
	public void setStarted(Boolean started) {
		this.started = started;
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
		return "MQConfig [mqName=" + mqName + ", direction=" + direction + ", hasAck=" + hasAck + ", started=" + started
				+ ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
	}
}
