package pt.hdi.sftpservice.thread;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.sftpservice.model.Configuration;
import pt.hdi.sftpservice.service.ConfigurationService;
import pt.hdi.sftpservice.service.DataCentralizerService;

public class FileProcessor implements Runnable {
    private final Path fileP;

    private ApplicationContext ctx;

    private ConfigurationService confService;
	
	private DataCentralizerService dcService;

    public FileProcessor(Path fileP, ApplicationContext appCtx) {
        this.ctx = appCtx;
        this.fileP = fileP;
        if (!Files.exists(fileP)){
            System.out.println("File was not found on the path: " +fileP.toString());
            return;
        }
        confService = ctx.getBean(ConfigurationService.class);
		dcService = ctx.getBean(DataCentralizerService.class);
    }

    @Override
    public void run() {
        Configuration conf = null;
        String fileName = "";
        String firstLine = "";
        String lastLine = "";
        // Before start to process the file, it will validate if it can be processed
        if (!Files.exists(fileP)){
            System.out.println("File was not found on the path: " +fileP.toString());
            return;
        }

        String fileAbsolutePath = fileP.toAbsolutePath().toString();

        System.out.println("Starting to read the file on: " + fileAbsolutePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileAbsolutePath))) {
            firstLine = reader.readLine(); // Read the first line

            if (firstLine == null) {
                // File is empty, handle accordingly
                System.out.println("File is empty");
                return;
            }
            lastLine = getLastLine(fileAbsolutePath);

            Long qtLinesByTrailler = Long.parseLong(lastLine);
            Long fileLines = reader.lines().count() - 1;
            if (qtLinesByTrailler != fileLines){
                System.out.println("Quantity of lines on the file ("+ fileLines +") does not match with Trailler (" +qtLinesByTrailler+")");
                return;
            }

            // Header has its format:
            // HDI_<filename>_<app_source>
            int start = firstLine.indexOf("HDI_") + 4; // +4 because "HDI_" is 4 characters long
            int end = firstLine.indexOf("_", start); // Find the next underscore after the start
            if (start >= 0 && end > start) { // Make sure the indices are valid
                fileName = firstLine.substring(start, end);
                System.out.println("Found the following filename on the file: " +fileName);
            } else {
                System.out.println("The input does not have the expected format.");
                return;
            }    
            ResponseEntity resConf = confService.getByDocumentName(fileName);
            if (!resConf.getStatusCode().equals(HttpStatus.OK)){
                System.out.println("Configuration not found for: " + fileName);
                throw new HttpClientErrorException(resConf.getStatusCode());
            }

            System.out.println("Configuration found for the file");
            conf = (Configuration) resConf.getBody();
            if (conf == null || conf.getSftpConfig() == null) {
                System.out.println("No config found for this file");
                if (Files.exists(fileP)) {
                    Files.delete(fileP);
                }
                return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileAbsolutePath))) {
            // Process the file received
            System.out.println("Will start to read file and send content");
            String line;
            ObjectMapper mapper = new ObjectMapper();
            while ((line = reader.readLine()) != null) {
                if (line.equals(firstLine) || line.equals(lastLine)){
                    continue;
                }
                // Assuming the JSON lines are mapped to a class called JsonDataClass
                if (confService.isValidMessageAndQueue(conf, fileName, line)){
                    dcService.sendMessage(conf, line);
                } // What to do if is not valid?
            }
        } catch (IOException e) {
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
    private static String getLastLine(String filePath) {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            long fileLength = file.length() - 1;
            StringBuilder sb = new StringBuilder();

            for (long pointer = fileLength; pointer >= 0; pointer--) {
                file.seek(pointer);
                char c = (char) file.read();
                if (c == '\n') {
                    if (fileLength == pointer) {
                        // Skip if this is just the last character in the file and it's a newline
                        continue;
                    }
                    break;
                }
                sb.append(c);
            }
            sb.reverse();
            return sb.toString();
        } catch (NumberFormatException e) {
            System.out.println("The string on Trailler does not contain a valid number.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
