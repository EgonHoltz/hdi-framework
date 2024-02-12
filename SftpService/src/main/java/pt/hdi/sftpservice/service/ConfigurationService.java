package pt.hdi.sftpservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.sftpservice.model.Configuration;
import pt.hdi.sftpservice.repository.ConfigurationRepository;

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

	public Configuration getByRqName(String rqName) {
		return confRep.findByMqConfigMqName(rqName);
	}
	
	public List<Configuration> getAllConfigurationWithMQAndNotStarted(){
		List<Configuration> configs = new ArrayList<Configuration>();
		configs.addAll(confRep.findConfigurationByMqConfigStarted(Boolean.FALSE));
		configs.addAll(confRep.findConfigurationByMqConfigStarted(null));
		return configs;
	}
	
}
