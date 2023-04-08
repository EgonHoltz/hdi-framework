package pt.hdi.mqsftp.sftp.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import pt.hdi.mqsftp.sftp.model.Configuration;

@Service
public class DataCentralizerService {

	@Autowired
	private RestTemplate rt;

	@Value("${spring.datacentral.url}")
	String url;
	
	public DataCentralizerService(RestTemplateBuilder rtb) {
		this.rt = rtb.build();
	}
	
	public ResponseEntity<String> sendReceivedFileToCentralizedServer(Configuration conf, File file) throws IOException, RestClientException {
		MultipartFile mtpFile = convert(file);
		String finalUrl = url + "/centralizeByFile";
	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	    body.add("file", new ByteArrayResource(mtpFile.getBytes()) {
	        @Override
	        public String getFilename() {
	            return mtpFile.getOriginalFilename();
	        }
	    });
	    
	    body.add("configuration", conf);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
	    ResponseEntity<String> res = rt.exchange(finalUrl, HttpMethod.POST, requestEntity, String.class);
	    
	    return res;
	    		
	}
	
    public MultipartFile convert(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", input);
        return multipartFile;
    }
	
}
