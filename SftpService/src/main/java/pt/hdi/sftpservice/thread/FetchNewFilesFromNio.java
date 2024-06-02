package pt.hdi.sftpservice.thread;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import pt.hdi.sftpservice.model.FileAuditLogger;
import pt.hdi.sftpservice.service.AuditLoggerService;
import pt.hdi.sftpservice.service.MinioService;

public class FetchNewFilesFromNio implements Runnable {

    	private ApplicationContext ctx;
        private AuditLoggerService auditLoggerService;
        private MinioService minioService;
        private Environment env;

        public FetchNewFilesFromNio(ApplicationContext appCtx) {
		this.ctx = appCtx;
        auditLoggerService = ctx.getBean(AuditLoggerService.class);
        minioService = ctx.getBean(MinioService.class);
        env = ctx.getEnvironment();
	}

    @Override
    public void run() {
        try{
            String sendPath = env.getProperty("spring.sftp.sendpath");
            while (true) {
                // Verify if it finds a file
                ResponseEntity res = auditLoggerService.getNotSentFilesFromNio();
                if (!HttpStatus.OK.equals(res.getStatusCode())){
                    System.out.println("Something went wrong when try to load data from server. It will try again in 2 minutes");
                    Thread.sleep(120000);
                    continue;
                }
                List<FileAuditLogger> files = (List<FileAuditLogger>) res.getBody();
                
                if (CollectionUtils.isEmpty(files)){
                    System.out.println("No file found. Will try again in 2 minutes");
                    Thread.sleep(30000);
                    continue;
                }
                System.out.println(files.size() + " new(s) file(s) found!");

                // get the file from MINIO
                for (FileAuditLogger f : files){
                    String[] filePathOnMinio = f.getMinioLink().split("/");
                    System.out.println("Will retrieve the file " + filePathOnMinio[1] +" from Minio bucket " + filePathOnMinio[0]);
                    InputStream fileIS = minioService.getFile(filePathOnMinio[0], filePathOnMinio[1]);

                    // put it on the local path to send
                    System.out.println("Put file to the path " + sendPath);
                    Path destinationPath = Paths.get(sendPath + "/" + filePathOnMinio[1]);
                    Files.copy(fileIS, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    fileIS.close();
                }               
                // Wait 2 minutes                
                Thread.sleep(120000);
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally{
            Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new FetchNewFilesFromNio(ctx));
        }

    }
    
}
