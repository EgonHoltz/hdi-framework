package pt.hdi.grpcservice.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import pt.hdi.grpcservice.model.Configuration;

@Service
public class DataCentralizerService {

	@Autowired
	private RestTemplate rt;

	@Value("${spring.datacentral.url}")
	String url;
	
	public DataCentralizerService(RestTemplateBuilder rtb) {
		this.rt = rtb.build();
	}
	
	public ResponseEntity<String> sendMessageToCentralizedServcer(Configuration conf, String body) {
		String finalUrl = url + "/centralizeByMq";
		ResponseEntity<String> res = null;
		try{
			HttpHeaders headers = new HttpHeaders();
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("Configuration", conf.getId());
			requestBody.put("json", body);
			
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
			res = rt.exchange(finalUrl, HttpMethod.POST, requestEntity, String.class);
			return res;
		} catch (Exception e) {
			System.out.println("Something went wrong on send");
			e.getStackTrace();
			res = new ResponseEntity<String>(e.getCause().toString(),HttpStatus.REQUEST_TIMEOUT);
			return res;
		}
	}
	
}
