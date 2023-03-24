package pt.hdi.mqsftp.sftp.config;

import java.io.File;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;

import pt.hdi.mqsftp.sftp.bean.SFTPConnectionBean;
import pt.hdi.mqsftp.sftp.model.SFTPConfig;

public class OutboundGtw {

	@Bean
	public SftpOutboundGateway sftpOutbGtw(SFTPConfig conf, Path file) {
		SFTPConnectionBean cnFctry = new SFTPConnectionBean(conf);
		SftpOutboundGateway sog = new SftpOutboundGateway(cnFctry.sftpSessionFactory(), "", "");
		sog.setOptions("-1");
		sog.setLocalDirectory(file.toFile());
		return sog;
	}
}
