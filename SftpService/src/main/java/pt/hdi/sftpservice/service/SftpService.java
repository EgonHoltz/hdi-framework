package pt.hdi.sftpservice.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import pt.hdi.sftpservice.model.SFTPConfig;

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
