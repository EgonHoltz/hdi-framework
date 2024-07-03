package pt.hdi.loggerservice.model;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import pt.hdi.loggerservice.utils.ApplicationEnums.FLOW_DIRECTION;

public class MQConfig {
	private String mqName;
	private FLOW_DIRECTION direction;
	private Boolean hasAck;
	private Boolean started;
	private String user;
	private String password;
	private Boolean active;
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
	public void setDirection(FLOW_DIRECTION direction) {
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
	public Boolean getStarted() {
		return started;
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
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "MQConfig [mqName=" + mqName + ", direction=" + direction + ", hasAck=" + hasAck + ", started=" + started
				+ ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
	}
}
