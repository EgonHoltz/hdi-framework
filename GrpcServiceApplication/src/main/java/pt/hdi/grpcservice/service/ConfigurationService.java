package pt.hdi.grpcservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.grpcservice.utils.JsonFieldValidator;
import pt.hdi.grpcservice.model.Configuration;
import pt.hdi.grpcservice.model.GRPCConfig;
import pt.hdi.grpcservice.model.Structure;

@Service
public class ConfigurationService {
	
	@Autowired
	private RestTemplate restTemplate;

    @Autowired
    private DocumentService docSvc;
	
	private final String baseUrl = "http://localhost:8002/configuration";

	
	public ResponseEntity getAllConfigs(){
		String otherServiceUrl = baseUrl + "/grpc";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            List<Configuration> configuration = new ArrayList();
            configuration = restTemplate.getForObject(builder.toUriString(), List.class);
            return new ResponseEntity<>(configuration,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}

	public ResponseEntity getByClientId(String clientId){
		String otherServiceUrl = baseUrl + "/grpc/" + clientId;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl);
        
		try {
            Configuration configuration = new Configuration();
            configuration = restTemplate.getForObject(builder.toUriString(), Configuration.class);
            return new ResponseEntity<>(configuration,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
	}

    public boolean isValidMessageAndClientId(Configuration conf, String clientId, String message, boolean isRetriveQuery) {
        boolean isValid = false;

        //start validating if configuration exists
        if (conf == null || conf.getGrpcConfig() == null){
            System.out.println("No configuration found");
            isValid = false;
            return isValid;
        }
        List<GRPCConfig> grpcConfigs = conf.getGrpcConfig();

        GRPCConfig grpcConfig = null;
        for (GRPCConfig grpc : grpcConfigs) {
			if (grpc.getClientId().equals(clientId)) {
                isValid = true;
                grpcConfig = grpc;
                break;
			}
		}

        if (grpcConfig == null){
            System.out.println("gRPC client identification not found on configuration");
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

        boolean validationRes = isRetriveQuery ? 
            JsonFieldValidator.validateInformedQuery(message, structure)  : JsonFieldValidator.validate(message, structure);

        if (validationRes){
            System.out.println("Validated with success!!");
            isValid = true;
        } else {
            System.out.println("Problems with message structure. Didn't passed from validation");
            isValid =false;
        }

        return isValid;
    }	
}
