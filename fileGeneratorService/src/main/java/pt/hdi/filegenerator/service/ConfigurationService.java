package pt.hdi.filegenerator.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.MeterRegistry.Config;
import pt.hdi.filegenerator.model.Configuration;

@Service
public class ConfigurationService {

    @Autowired
	private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8002/configuration";
    
    public List<Configuration> getAllConfigsWithSendSFTP(){
    String uri = baseUrl + "/sftp/schedulers";
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
    
        try {
            List<Configuration> configuration = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(restTemplate.getForObject(builder.toUriString(), List.class));
            configuration = objectMapper.readValue(jsonString, 
            new TypeReference<List<Configuration>>() {});
            return configuration;
        } catch (Exception e) {
            return null;
        }
	}

    public Configuration getConfigurationById(String configurationId){
        String uri = baseUrl + String.format("/sftp/%s/scheduler", configurationId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(restTemplate.getForObject(builder.toUriString(), Configuration.class));
            Configuration configuration = objectMapper.readValue(jsonString, 
            new TypeReference<Configuration>() {});
            return configuration;
        } catch (Exception e) {
            return null;
        }
    }

    public String getLastDocumentIdForDeltaByConfiguration(String configurationId){
        Configuration conf = getConfigurationById(configurationId);
        if (conf != null) {
            return conf.getSftpSchedulerConfig().getLastObjectId();
        }
        return null;
    }

    public void updateLastDocumentIdForDeltaByConfiguration(String lastDocId, String configurationId){
        String uri = baseUrl + String.format("/sftp/%s/scheduler", configurationId);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);

        String urlBuilder = UriComponentsBuilder.fromUriString(uri)
            .queryParam("lastDocId", lastDocId)
            .toUriString();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            restTemplate.exchange(urlBuilder,HttpMethod.PUT,null,Void.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
