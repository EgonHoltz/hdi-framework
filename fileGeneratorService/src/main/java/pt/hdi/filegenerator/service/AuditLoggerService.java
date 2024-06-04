package pt.hdi.filegenerator.service;

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

import pt.hdi.filegenerator.model.FileAuditLogger;

@Service
public class AuditLoggerService {
    
    @Autowired
	private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8002/auditLogger/";

        public ResponseEntity addFileSendSftpEntry(FileAuditLogger audit){
        String uri = baseUrl + "/sftp/scheduler";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FileAuditLogger> requestBody = new HttpEntity<>(audit,headers);

        try {
            return restTemplate.exchange(uri,HttpMethod.POST ,requestBody, Void.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
