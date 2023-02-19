package pt.hdi.mqsftp.sftp.config;

public class MQConnectionBean {
	String host;
	String username;
	String password;
	public MQConnectionBean(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
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
	@Override
	public String toString() {
		return "MQConnectionBean [host=" + host + ", username=" + username + ", password=" + password + "]";
	}
	
	

}