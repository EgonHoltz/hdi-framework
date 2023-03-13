package pt.hdi.mqsftp.sftp.model;

public class MQConfig {
	String mqName;
	String direction;
	Boolean hasAck;
	Boolean started;
	
	
	public String getMqName() {
		return mqName;
	}
	public void setMqName(String mqName) {
		this.mqName = mqName;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
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
	@Override
	public String toString() {
		return "MQConfig [mqName=" + mqName + ", direction=" + direction + ", hasAck=" + hasAck + "]";
	}

	
	
}
