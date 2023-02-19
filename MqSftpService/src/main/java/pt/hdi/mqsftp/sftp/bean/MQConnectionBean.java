package pt.hdi.mqsftp.sftp.bean;

public class MQConnectionBean {
	String host;
	String username;
	String password;
	Integer port;
	public MQConnectionBean(String host, String username, String password, Integer port) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public Integer getPort() {
		return port;
	}
	@Override
	public String toString() {
		return "MQConnectionBean [host=" + host + ", username=" + username + ", password=" + password + "]";
	}
	
	

}
