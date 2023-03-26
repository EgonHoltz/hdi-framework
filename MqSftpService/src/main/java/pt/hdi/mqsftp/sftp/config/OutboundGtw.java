package pt.hdi.mqsftp.sftp.config;

import java.io.File;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;

import pt.hdi.mqsftp.sftp.bean.SFTPConnectionBean;
import pt.hdi.mqsftp.sftp.model.SFTPConfig;

public class OutboundGtw {

	@Bean
	public void sftpOutbGtw(SFTPConfig conf, Path file) throws Exception{
		String filePath = file.toFile().toString();
		String expression = "new java.io.File('" + filePath + "')";
		SFTPConnectionBean cnFctry = new SFTPConnectionBean(conf);
		SftpOutboundGateway sog = new SftpOutboundGateway(cnFctry.sftpSessionFactory(), "put", expression);
		sog.setLocalDirectory(file.toFile());
		sog.setRemoteDirectoryExpression(new LiteralExpression(conf.getDestinationPath()));
		Message<File> message = MessageBuilder.withPayload(file.toFile()).build();
		sog.handleMessage(message);
	}
}
