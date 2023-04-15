package pt.hdi.restservice.model;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public class SFTPConfig {
	
	private String host;
	private int port;
	private String user;
	private String password;
	private String direction;
	private String sftpFileName;
	private String destinationPath;
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
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

}
