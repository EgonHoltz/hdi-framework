package pt.hdi.restservice.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pt.hdi.restservice.bean.DocumentFilterFields;
import pt.hdi.restservice.bean.PaginationResponse;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.Structure;

@Service
public class DataManagementService {
    
    @Autowired
    @Qualifier("restTemplateSimple")
    private RestTemplate restTemplate;
    
    private final String baseUrl = "http://localhost:8012/";
    public ResponseEntity getCollectionFields(String collectionName,String modelKey) {
        String otherServiceUrl = baseUrl + "collection/structure";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(otherServiceUrl)
                .queryParam("collectionName", collectionName)
                .queryParam("modelKey", modelKey);
        try {
            List<String> structure = new ArrayList();
            structure = restTemplate.getForObject(builder.toUriString(), List.class);
            return new ResponseEntity<>(structure,HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getCollectionExists(String collectionName) {
        try {
            String otherServiceUrl = baseUrl + "collection/";
            String urlBuilder = UriComponentsBuilder.fromUriString(otherServiceUrl)
                    .queryParam("collectionName", collectionName).toUriString();
            return restTemplate.exchange(urlBuilder,HttpMethod.GET,null, Void.class);

        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.NotFound | HttpServerErrorException.InternalServerError ex) {
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.getCause().getMessage();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity createCollection(DocumentData doc){
        String otherServiceUrl = baseUrl + "collection/structure";
        String collectionName = doc.getDocumentName().replaceAll("\\s", "").toLowerCase(); 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Structure>> requestBody = new HttpEntity<>(doc.getStructures(), headers);


        String urlBuilder = UriComponentsBuilder.fromUriString(otherServiceUrl)
            .queryParam("collectionName", collectionName)
            .toUriString();

            try {
                String modelKey = restTemplate.postForObject(urlBuilder,requestBody,String.class);
                return new ResponseEntity<>(modelKey,HttpStatus.OK);
    
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }

    public ResponseEntity editCollection(String documentName, String modelKey, Structure structure){
        String otherServiceUrl = baseUrl + "collection/structure";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Structure> requestBody = new HttpEntity<>(structure, headers);


        String urlBuilder = UriComponentsBuilder.fromUriString(otherServiceUrl)
            .queryParam("collectionName", documentName)
            .queryParam("modelKey", modelKey)
            .toUriString();

        try {
            return restTemplate.exchange(urlBuilder,HttpMethod.PUT ,requestBody, Void.class);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<PaginationResponse<Document>> findDocumentDataWithFilters(DocumentData doc, List<DocumentFilterFields> filters, int page, int size) {
        String otherServiceUrl = baseUrl + "search/query";
        String collectionName = doc.getDocumentName().replaceAll("\\s", "").toLowerCase(); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<DocumentFilterFields>> requestBody = new HttpEntity<>(filters, headers);

        String urlBuilder = UriComponentsBuilder.fromUriString(otherServiceUrl)
            .queryParam("collectionName", collectionName)
            .queryParam("page", page)
            .queryParam("size", size)
            .toUriString();

        try {
            ResponseEntity<PaginationResponse<Document>> response = restTemplate
                .exchange(urlBuilder,HttpMethod.POST ,requestBody, new ParameterizedTypeReference<PaginationResponse<Document>>(){});
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteRecordOnCollection(DocumentData doc, String dataId){
        String otherServiceUrl = baseUrl + "search/delete/"+dataId;
        String collectionName = doc.getDocumentName().replaceAll("\\s", "").toLowerCase(); 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Structure>> requestBody = new HttpEntity<>(headers);


        String urlBuilder = UriComponentsBuilder.fromUriString(otherServiceUrl)
            .queryParam("collectionName", collectionName)
            .toUriString();

            try {
                return restTemplate.exchange(urlBuilder,HttpMethod.DELETE ,requestBody, Void.class);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }
}
