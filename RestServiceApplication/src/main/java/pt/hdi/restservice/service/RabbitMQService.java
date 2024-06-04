package pt.hdi.restservice.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

@Service
public class RabbitMQService {

    @Autowired
    @Qualifier("restTemplateWithRabbitAuth")
    private RestTemplate restTemplate;

    private final String URL = "http://localhost:15672/api/";

        public ResponseEntity createQueue(String queueName) {
            if (queueName.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String finalUrl= UriComponentsBuilder
                .fromUriString(URL)
                .pathSegment("queues", "/", queueName)
                .toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String jsonBody = "{\"auto_delete\":false, \"durable\":true, \"arguments\":{}}";

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
            try {
                URI uri = new URI(finalUrl);
                return restTemplate.exchange(uri, HttpMethod.PUT, request, String.class);
            } catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        public ResponseEntity createUser(String user, String pass){
            if (user.isEmpty() || pass.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            String finalUrl= URL + "users/" + user;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String jsonBody = String.format("{\"password\":\"%s\", \"tags\": []}",pass);
            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            try{
                ResponseEntity resGet = restTemplate.exchange(finalUrl, HttpMethod.GET, null, String.class);

                // Verify if the user exists
                if (HttpStatus.OK.equals(resGet.getStatusCode())){
                    // User is already there, delete it to create again with new password
                    ResponseEntity resDelete = restTemplate.exchange(finalUrl, HttpMethod.DELETE, null, String.class);
                    if (HttpStatus.NO_CONTENT.equals(resDelete.getStatusCode())){
                        return restTemplate.exchange(finalUrl, HttpMethod.PUT, request, String.class);
                    } else {
                        return resDelete;
                    }
                }
                return resGet;
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    return restTemplate.exchange(finalUrl, HttpMethod.PUT, request, String.class);
                } 
                return new ResponseEntity<>(e.getStatusCode());                
            } catch(Exception e){
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }
        }

        public ResponseEntity addPermission(String user, String qName){
                HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String finalUrl= UriComponentsBuilder
            .fromUriString(URL)
            .pathSegment("permissions", "/", user)
            .toUriString();

            // Construct the JSON body from the permissions object
            Map<String, Object> body = new HashMap<>();
            body.put("configure", ".*");
            body.put("write", ".*");
            body.put("read", ".*");

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
}
