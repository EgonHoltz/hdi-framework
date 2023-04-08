package pt.hdi.mqsftp.sftp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SFTPSendFileController {
	
	@Value("${spring.sftp.sendpath}")
	private String sendPath;

	@PostMapping(path = "/sendSFTP", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Void> sendMsgByMQ(@RequestParam("name") String name, @RequestParam("file") MultipartFile file){
		if (name.isBlank() || file == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);	    	
		}
	    // -Djava.io.tmpdir set on app startup
		String tempDir = System.getProperty("java.io.tmpdir");
		// File generated by centralizer
		String finalFileName = "N_"+ name + "_N" + file.getOriginalFilename();
	    File tempFile = new File(tempDir + "/" + finalFileName);
	    // Assert file name as expected by destination and move to the folder to send
	    
		try {
			file.transferTo(tempFile);
			Path sftpSendPath = Paths.get(sendPath+"/"+finalFileName);
			if (Files.exists(sftpSendPath)) {
				Files.delete(sftpSendPath);
			}
			Files.move(tempFile.toPath(), sftpSendPath);
			
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch ( IOException e) {
			System.err.println("Problems with IO: "+e.getStackTrace());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} 		
	}
	
}
