package pt.hdi.sftpservice.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.sftpservice.model.FileAuditLogger;

@Service
public class AuditLoggerService {

    @Autowired
	private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8002/auditLogger";

    public ResponseEntity getNotSentFilesFromNio(){
        String uri = baseUrl + "/sftp/filesNotSent";        
        try {
            List<FileAuditLogger> fileAuditLoggers = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(restTemplate.exchange(uri,HttpMethod.GET ,null, List.class).getBody());
            fileAuditLoggers = objectMapper.readValue(jsonString, new TypeReference<List<FileAuditLogger>>(){});
            return new ResponseEntity<>(fileAuditLoggers,HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity setFileSentBySftp(FileAuditLogger audit){
        String uri = baseUrl + "/sftp/filesNotSent/sent/"+ audit.getId();        
        try {
            return restTemplate.exchange(uri,HttpMethod.PUT ,null, List.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getFileAuditLoggerByFileName(String fileName) {      
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            //String encodedFileName = UriUtils.encode(fileName, "UTF-8"); 
            //String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            String urlBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/sftp")
                .queryParam("fileName", fileName)
                .toUriString();

            System.out.println("Encoded URL: " + urlBuilder);
            ResponseEntity<FileAuditLogger> responseEntity = restTemplate.exchange(
                urlBuilder, HttpMethod.GET, entity, FileAuditLogger.class);

            FileAuditLogger fileAuditLogger = responseEntity.getBody();
            return new ResponseEntity<>(fileAuditLogger,HttpStatus.OK);
        // } catch (UnsupportedEncodingException e) {
        //     e.printStackTrace();
        //     return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
