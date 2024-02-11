package pt.hdi.restservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository confRep;
	
	public List<Configuration> getAllConfigs(){
		return confRep.findAll();
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
	
	public boolean createNewConfiguration(Configuration config) {
		if (getByDocumentConfiguration(config.getId()) == null) {
			confRep.insert(config);
			return true;
		}
		return false;
	}
	
	public boolean saveConfiguration(Configuration conf) {
		Configuration savedConf = confRep.save(conf);
		if (savedConf != null) {
			return true;
		}
		return false;
	}
	
}
