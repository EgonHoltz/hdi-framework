package pt.hdi.sftpservice.thread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pt.hdi.sftpservice.model.Configuration;
import pt.hdi.sftpservice.model.FileAuditLogger;
import pt.hdi.sftpservice.model.SFTPConfig;
import pt.hdi.sftpservice.service.AuditLoggerService;
import pt.hdi.sftpservice.service.ConfigurationService;
import pt.hdi.sftpservice.service.SftpService;
import pt.hdi.sftpservice.utils.ApplicationEnums.FLOW_DIRECTION;

public class FileSendProcessor implements Runnable {
    private final Path fileP;
    private ApplicationContext ctx;

    private AuditLoggerService auditSvc;
    private Environment env;
    private ConfigurationService confSvc;
    private SftpService sftpSvc;

    public FileSendProcessor (Path fileP, ApplicationContext appCtx){
        this.fileP = fileP;
        this.ctx = appCtx;
        this.auditSvc = appCtx.getBean(AuditLoggerService.class);
        this.confSvc = appCtx.getBean(ConfigurationService.class);
        this.sftpSvc = appCtx.getBean(SftpService.class);
        env = ctx.getEnvironment();
    }

    @Override
    public void run() {

        String minioBucket = env.getProperty("minio.bucket.name");
        try {
            // Load configuration of file
            System.out.println("Loading the information of file " + fileP.getFileName());
            ResponseEntity resAudit = auditSvc.getFileAuditLoggerByFileName(minioBucket + "/" + fileP.getFileName());
            if (!HttpStatus.OK.equals(resAudit.getStatusCode())){
                System.out.println("No information found about the requested file on the centralization");
                if (Files.exists(fileP)) {
                    Files.delete(fileP);
                }
                return ;
            }
            FileAuditLogger audit = (FileAuditLogger) resAudit.getBody();

            // Get SFTP configurations
            System.out.println("Loading the information of file by config ID "+ audit.getConfigurationId());

            Configuration config = audit.getConfiguration();

            SFTPConfig sendConf = null;
            for (SFTPConfig sftpConf : config.getSftpConfig()){
                if (FLOW_DIRECTION.SEND.equals(sftpConf.getDirection())){
                    sendConf = sftpConf;
                    break;
                }
            }
            
            if (sendConf == null) {
                System.out.println("No SFTP Configuration found about the requested file on the centralization");
                if (Files.exists(fileP)) {
                    Files.delete(fileP);
                }
                return ;
            }
            System.out.println("Loaded destination to " + sendConf.getHost() + " on path " + sendConf.getDestinationPath());
            // Send SFTP to destination
            System.out.println("Starting the process to send the file");

            boolean resSend = sftpSvc.doSftpSendFile(sendConf, fileP);

            if (resSend){
                System.out.println("File sent with success!");
            } else {
                System.out.println("Something went wrong while the file was sent");
            }

            // Log SFTP with success

            ResponseEntity resUpdateAudit = auditSvc.setFileSentBySftp(audit);
            if (!HttpStatus.OK.equals(resUpdateAudit.getStatusCode())){
                System.out.println("Error on update the file status on the centralizator");
                if (Files.exists(fileP)) {
                    Files.delete(fileP);
                }
            }

            System.out.println("End of send SFTP operation");

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (Files.exists(fileP)) {
                try {
                    Files.delete(fileP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
