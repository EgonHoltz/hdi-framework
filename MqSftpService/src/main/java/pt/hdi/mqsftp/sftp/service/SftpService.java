package pt.hdi.mqsftp.sftp.service;

import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import pt.hdi.mqsftp.sftp.model.SFTPConfig;

@Service
public class SftpService {

	public boolean doSftpSendFile(SFTPConfig sftpConf, Path file) throws Exception {
		boolean success = false;
		Session session = null;
		ChannelSftp sftpChannel = null;
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(sftpConf.getUser(), sftpConf.getHost(), sftpConf.getPort());
			session.setPassword(sftpConf.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			
			sftpChannel = (ChannelSftp) session.openChannel("sftp");
			
			sftpChannel.connect();
			
			String remoteFile = sftpConf.getDestinationPath() + "/" + file.getFileName().getFileName();
			String localFile = file.toFile().toString();
			
			sftpChannel.put(localFile,remoteFile);
			
			success = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something went wrong");
			throw e;
		} finally {
			if (session !=null) {
				session.disconnect();
			}
			if (sftpChannel != null) {
				sftpChannel.disconnect();
			}
		}
		return success;
		
	}
}
