package pt.hdi.mqservice.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import pt.hdi.mqservice.model.Configuration;

@Service
public class ConfigurationService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String baseUrl = "http://localhost:8002/configuration";

	
	public ResponseEntity getAllConfigs(){
		String otherServiceUrl = baseUrl + "/mqqueue";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            List<Configuration> configuration = new ArrayList();
            configuration = restTemplate.getForObject(builder.toUriString(), List.class);
            return new ResponseEntity<>(configuration,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}
		
	public ResponseEntity getAllConfigurationWithMQAndNotStarted(){

		String otherServiceUrl = baseUrl + "/mqqueue/notstarted";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            List<Configuration> configuration = new ArrayList();
            configuration = restTemplate.getForObject(builder.toUriString(), List.class);
            return new ResponseEntity<>(configuration,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}

	public ResponseEntity saveConfiguration(String mqName){
			HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String finalUrl= UriComponentsBuilder
            .fromUriString(baseUrl)
            .pathSegment( "mqqueue", mqName)
            .toUriString();

            // Construct the JSON body from the permissions object
            Map<String, Object> body = new HashMap<>();
            body.put("active", true);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            try {
                URI uri = new URI(finalUrl);
                return restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);
            } catch (HttpClientErrorException e) {
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

	public ResponseEntity getByRqName(String mqName){
		String otherServiceUrl = baseUrl + "/mqqueue/" + mqName;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            Configuration configuration = new Configuration();
            configuration = restTemplate.getForObject(builder.toUriString(), Configuration.class);
            return new ResponseEntity<>(configuration,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}
	
}
