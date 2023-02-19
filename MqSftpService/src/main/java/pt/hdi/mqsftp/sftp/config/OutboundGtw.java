package pt.hdi.mqsftp.sftp.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;

public class OutboundGtw {

	@Autowired
	private SftpFactory sftpFactory;
	
	@Bean
	public SftpOutboundGateway sftpOutbGtw() {
		SftpOutboundGateway sog = new SftpOutboundGateway(sftpFactory.sftpSessionFactory(), "", "");
		sog.setOptions("-1");
		sog.setLocalDirectory(new File("/tmp/send"));
		return sog;
	}
}
