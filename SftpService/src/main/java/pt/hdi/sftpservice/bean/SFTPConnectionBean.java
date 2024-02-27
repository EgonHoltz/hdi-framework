package pt.hdi.sftpservice.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import com.jcraft.jsch.ChannelSftp;

import pt.hdi.sftpservice.model.SFTPConfig;

public class SFTPConnectionBean {

	private String host;
	private String username;
	private String password;
	private int port;
	
	public SFTPConnectionBean(SFTPConfig conf) {
		this.host = conf.getHost();
		this.username = conf.getUser();
		this.password = conf.getPassword();
		this.port = conf.getPort();
	}
	
	@Bean
	public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory(){
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(false);
		factory.setHost(host);
		factory.setPort(port);
		factory.setUser(username);
		factory.setPassword(password);
		factory.setAllowUnknownKeys(true);
		return new CachingSessionFactory<ChannelSftp.LsEntry>(factory);
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

	public int getPort() {
		return port;
	}
	
}
