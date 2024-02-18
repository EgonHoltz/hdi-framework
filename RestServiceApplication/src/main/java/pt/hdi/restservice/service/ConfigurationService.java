package pt.hdi.restservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pt.hdi.restservice.bean.ConfigurationMQBean;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.MQConfig;
import pt.hdi.restservice.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository confRep;
	
	public List<ConfigurationMQBean> getAllConfigs(){
		List<Configuration> allConfig = confRep.findAll();
		List<ConfigurationMQBean> mqConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : allConfig) {
			mqConfig.add(new ConfigurationMQBean(cfg));
		}
		return mqConfig;
	}

	public Configuration getConfigurationByDocApp(DocumentData doc, Application app){
		return confRep.findByDocumentApplication(doc.getId(), app.getId());
	}
	
	public Configuration getByDocumentConfiguration(String docId) {
		if (docId != null){
			return confRep.findById(docId).get();
		} else {
			return null;
		}
	}

	public ConfigurationMQBean getByRqName(String mqName) {
		Configuration conf = confRep.findByMqConfigMqName(mqName);
		return new ConfigurationMQBean(conf);
	}

	public void setMqQueueStarted(String mqName){
		Configuration config = confRep.findByMqConfigMqName(mqName);
		List<MQConfig> mqChange = new ArrayList<>();
		for (MQConfig cfg : config.getMqConfig()) {
			if (cfg.getMqName().equals(mqName)){
				cfg.setStarted(true);
				mqChange.add(cfg);
			} else {
				mqChange.add(cfg);
			}
		}
		config.setMqConfig(mqChange);
		confRep.save(config);
	}

	public List<ConfigurationMQBean> getAllConfigurationWithMQAndNotStarted(){
		List<Configuration> configs = new ArrayList<Configuration>();
		configs.addAll(confRep.findConfigurationByMqConfigStarted(Boolean.FALSE));
		configs.addAll(confRep.findConfigurationByMqConfigStarted(null));
		List<ConfigurationMQBean> mqConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : configs) {
			mqConfig.add(new ConfigurationMQBean(cfg));
		}
		return mqConfig;
	}
	
}
