package pt.hdi.mqsftp.sftp.service;

import org.springframework.stereotype.Service;

import pt.hdi.mqsftp.sftp.config.OutboundGtw;

@Service
public class SftpService {

	private OutboundGtw sog;
	
	public void sendFile() {
		sog.sftpOutbGtw().handleMessage(null);
	}
}
