package pt.hdi.restservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository confRep;
	
	public List<Configuration> getAllConfigs(){
		return confRep.findAll();
	}
	
	public Configuration getByDocumentName(String docName) {
		return confRep.findByDocumentName(docName);
	}
	
	public boolean createNewConfiguration(Configuration config) {
		if (getByDocumentName(config.getDocumentName()) == null) {
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
