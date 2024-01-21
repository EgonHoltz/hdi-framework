package pt.hdi.restservice.model;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import pt.hdi.restservice.Utils.ApplicationEnums.FLOW_DIRECTION;

public class SFTPConfig {
	
	private String host;
	private int port;
	private String user;
	private String password;
	private FLOW_DIRECTION direction;
	private String sftpFileName;
	private String destinationPath;
	private Boolean active;

	@LastModifiedDate
	private Date lastModificationDate;
	@CreatedDate
	private Date createDate;
	
	public SFTPConfig() {
		super();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	public FLOW_DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(FLOW_DIRECTION direction) {
		this.direction = direction;
	}
	public String getSftpFileName() {
		return sftpFileName;
	}

	public void setSftpFileName(String sftpFileName) {
		this.sftpFileName = sftpFileName;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
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
		return "SFTPConfig [host=" + host + ", port=" + port + ", user=" + user + ", password=" + password
				+ ", direction=" + direction + ", sftpFileName=" + sftpFileName + ", destinationPath=" + destinationPath
				+ ", lastModificationDate=" + lastModificationDate + ", createDate=" + createDate + "]";
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
