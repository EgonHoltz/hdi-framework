package pt.hdi.mqservice.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.mqservice.model.Configuration;
import pt.hdi.mqservice.model.MQConfig;
import pt.hdi.mqservice.service.ConfigurationService;

public class MQMonitoring implements Runnable{

	private ConfigurationService confService;
		
	private MongoTemplate mt;
	
	private ApplicationContext ctx;
	
	public MQMonitoring(ApplicationContext appCtx) {
		this.ctx = appCtx;
	}
    
	@Override
	public void run() {
		mt = ctx.getBean(MongoTemplate.class);
		confService = ctx.getBean(ConfigurationService.class);
		
		System.out.println("MQMonitoring started!");
		
		if (confService == null) {
			return;
		}
		try {
			// It means, the service is starting on the first time, so look at all
			// MQ Queue that are already instanciated once

			ResponseEntity resMqConfigs = confService.getAllConfigs();
			if (!resMqConfigs.getStatusCode().equals(HttpStatus.OK)){
				throw new HttpClientErrorException(resMqConfigs.getStatusCode());
			}
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(resMqConfigs.getBody());

			List<Configuration> configuration = new ArrayList();
			try {
				configuration = objectMapper.readValue(jsonString, 
					new TypeReference<List<Configuration>>() {});
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<String> rabbitQueuesNames = new ArrayList<String>();
			for (Configuration conf : configuration) {
				List<MQConfig> mqConfigs = conf.getMqConfig(); // Assuming getMqConfig() returns List<MQConfig> or similar
				if (mqConfigs == null) {
					continue; // Skip to the next config if this one's MQConfig list is null
				}
				for (MQConfig mq : mqConfigs) {
					if (mq.hasStarted()) {
						rabbitQueuesNames.add(mq.getMqName());
					}
				}
			}
			rabbitQueuesNames.stream().forEach(queueNames -> {
				Optional.ofNullable(queueNames).ifPresent(queue -> {
					Executor exec = Executors.newSingleThreadExecutor();
					exec.execute(new ReadMQMessage(queue, ctx));
					System.out.println("Started queue: " + queue);
				});
			});

			
			while(true) {
				//Wait 10 seconds
				Thread.sleep(10000);

				// This will allow to create queue instantly and does not need to change the app
			
				ResponseEntity resMqNotStarted = confService.getAllConfigurationWithMQAndNotStarted();
				if (!resMqNotStarted.getStatusCode().equals(HttpStatus.OK)){
					throw new HttpClientErrorException(resMqNotStarted.getStatusCode());
				}
				
				List<Configuration> mqNotStarted = null;
				try {
					jsonString = objectMapper.writeValueAsString(resMqNotStarted.getBody());
					mqNotStarted = objectMapper.readValue(jsonString, 
						new TypeReference<List<Configuration>>() {});
				} catch (IOException e) {
					e.printStackTrace();
					mqNotStarted = new ArrayList<>();
				}

				if(mqNotStarted == null || mqNotStarted.isEmpty()) {
					continue;
				}
				
				List<MQConfig> rabbitQueuesNotStarted = mqNotStarted.stream()
						.flatMap(conf -> Optional.ofNullable(conf.getMqConfig()).orElse(Collections.emptyList()).stream())
						.filter(mq -> !mq.hasStarted())
						.collect(Collectors.toList());
				for (MQConfig queueName : rabbitQueuesNotStarted) {
					if (queueName != null) { // Replace Optional.ofNullable with an explicit null check
						Executor exec = Executors.newSingleThreadExecutor();
						exec.execute(new ReadMQMessage(queueName.getMqName(), ctx));
						findQueueAndDeclareStarted(findConfigurationByQueue(queueName.getMqName(), mqNotStarted),
								queueName.getMqName());
						System.out.println("Added new queue and started: " + queueName);
					}
				}
			}	
		} catch (Exception e) {
			System.err.println("Problems with IO: "+e.getStackTrace());
			e.printStackTrace();
		} finally {
			System.out.println("MQMonitoring finished");
			//Executor exec = Executors.newFixedThreadPool(1);
			//exec.execute(new MQMonitoring());
		}
	}

	private Optional<Configuration> findConfigurationByQueue(String qName, List<Configuration> configs) {
		return configs.stream()
		.filter(config -> config.getMqConfig().stream()
		.anyMatch(mqConfig -> qName.equals(mqConfig.getMqName())))
		.findFirst();
	}

	private void findQueueAndDeclareStarted(Optional<Configuration> optConf, String qName) {

		if (optConf.isEmpty()){
			throw new ExceptionInInitializerError();
		}
		Configuration conf = optConf.get();

		List<MQConfig> newMqList = new ArrayList<MQConfig>();
		for (MQConfig mqconf : conf.getMqConfig()) {
			if (mqconf.getMqName().equals(qName)) {
				ResponseEntity resMqConfigs = confService.saveConfiguration(qName);
				if (!resMqConfigs.getStatusCode().equals(HttpStatus.OK)){
					throw new HttpClientErrorException(resMqConfigs.getStatusCode());
				}
			}
			newMqList.add(mqconf);
		}

	}

}
