package pt.hdi.mqsftp.sftp.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;

import pt.hdi.mqsftp.sftp.config.OutboundGtw;
import pt.hdi.mqsftp.sftp.model.SFTPConfig;

@Service
public class SftpService {

	private OutboundGtw sog;
	
	public void sendFile(SFTPConfig sftpConf, Path file) {
		
		sog.sftpOutbGtw(sftpConf, file).handleMessage(null);
		
	}
}
