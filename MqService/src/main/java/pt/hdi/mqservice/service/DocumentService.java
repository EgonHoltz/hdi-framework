package pt.hdi.mqservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pt.hdi.mqservice.model.Configuration;
import pt.hdi.mqservice.model.Structure;

@Service
public class DocumentService {
	@Autowired
	private RestTemplate restTemplate;
	
	private final String baseUrl = "http://localhost:8002/document";
    
    public ResponseEntity getStructureByDocumentId(String id){
		String otherServiceUrl = baseUrl + "/" + id + "/dbstatus/structure";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            List<Structure> structures = new ArrayList<>();
            structures = restTemplate.getForObject(builder.toUriString(), List.class);
            return new ResponseEntity<>(structures,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}


}
