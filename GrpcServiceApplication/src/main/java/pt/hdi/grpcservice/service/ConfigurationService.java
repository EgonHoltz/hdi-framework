package pt.hdi.grpcservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.grpcservice.model.Configuration;
import pt.hdi.grpcservice.repository.ConfigurationRepository;


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
	
	public boolean saveConfiguration(Configuration conf) {
		Configuration savedConf = confRep.save(conf);
		if (savedConf != null) {
			return true;
		}
		return false;
	}
	
}
