package pt.hdi.sendmqservice.service;

import java.io.IOException;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.sendmqservice.Utils.JsonFieldValidator;
import pt.hdi.sendmqservice.model.Configuration;
import pt.hdi.sendmqservice.model.MQConfig;
import pt.hdi.sendmqservice.model.Structure;

@Service
public class ConfigurationService {
	
	@Autowired
	private RestTemplate restTemplate;

    @Autowired
    private DocumentService docSvc;
	
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

    public boolean isValidMessageAndQueue(Configuration conf, String rcvId, String message) {
        boolean isValid = false;

        //start validating if configuration exists
        if (conf == null || conf.getMqConfig() == null){
            System.out.println("No configuration found");
            isValid = false;
            return isValid;
        }
        List<MQConfig> mqConfigs = conf.getMqConfig();

        MQConfig mqConfig = null;
        for (MQConfig mqconf : mqConfigs) {
			if (mqconf.getMqName().equals(rcvId)) {
                isValid = true;
                mqConfig = mqconf;
                break;
			}
		}

        if (mqConfig == null){
            System.out.println("MqName not found on configuration");
            isValid &= false;
            return isValid;
        }

        //start validating the message structure
        String docId = conf.getDocumentDataId();
        ResponseEntity resDoc = docSvc.getStructureByDocumentId(docId);
		if (!resDoc.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("Fail to retrieve structure from server");
            isValid &= false;
			throw new HttpClientErrorException(resDoc.getStatusCode());
		}

        List<Structure> structure = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(resDoc.getBody());
            structure = objectMapper.readValue(jsonString, 
                new TypeReference<List<Structure>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (structure == null){
            System.out.println("Structure retrieved is null!!");
            isValid &= false;
            return isValid;
        }

        if (JsonFieldValidator.validate(message, structure)){
            System.out.println("Validated with success!!");
            isValid = true;
        } else {
            System.out.println("Problems with message structure. Didn't passed from validation");
            isValid =false;
        }

        return isValid;
    }

    public boolean isValidQueryAndQueue(Configuration conf, String rcvId, String message) {
        boolean isValid = false;

        //start validating if configuration exists
        if (conf == null || conf.getMqConfig() == null){
            System.out.println("No configuration found");
            isValid = false;
            return isValid;
        }
        List<MQConfig> mqConfigs = conf.getMqConfig();

        MQConfig mqConfig = null;
        for (MQConfig mqconf : mqConfigs) {
			if (mqconf.getMqName().equals(rcvId)) {
                isValid = true;
                mqConfig = mqconf;
                break;
			}
		}

        if (mqConfig == null){
            System.out.println("MqName not found on configuration");
            isValid &= false;
            return isValid;
        }

        //start validating the message structure
        String docId = conf.getDocumentDataId();
        ResponseEntity resDoc = docSvc.getQueryFieldsByDocumentId(docId);
		if (!resDoc.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("Fail to retrieve structure from server");
            isValid &= false;
			throw new HttpClientErrorException(resDoc.getStatusCode());
		}

        List<Structure> structure = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(resDoc.getBody());
            structure = objectMapper.readValue(jsonString, 
                new TypeReference<List<Structure>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (structure == null){
            System.out.println("Structure retrieved is null!!");
            isValid &= false;
            return isValid;
        }

        if (JsonFieldValidator.validateInformedQuery(message, structure)){
            System.out.println("Validated with success!!");
            isValid = true;
        } else {
            System.out.println("Problems with message structure. Didn't passed from validation");
            isValid =false;
        }

        return isValid;
    }
}
