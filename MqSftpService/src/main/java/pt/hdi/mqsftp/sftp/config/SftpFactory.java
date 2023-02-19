package pt.hdi.mqsftp.sftp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public class SftpFactory {
	@Bean
	public SessionFactory<LsEntry> sftpSessionFactory(){
		DefaultSftpSessionFactory f = new DefaultSftpSessionFactory();
		f.setHost("localhost");
		f.setPort(4021);
		f.setUser("huser");
		f.setPassword("hpass");
		f.setAllowUnknownKeys(true);
		return new CachingSessionFactory<LsEntry>(f);
	}
	
}
