package pt.hdi.grpcservice.bean;

import org.springframework.core.env.Environment;

public class MQConnectionBean {

	private String host;
	private String username;
	private String password;
	
	public MQConnectionBean() {
		super();
	}

	public MQConnectionBean(Environment env) {
		super();
		this.host = env.getProperty("spring.rabbitmq.host");
		this.username = env.getProperty("spring.rabbitmq.username");
		this.password = env.getProperty("spring.rabbitmq.password");
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
