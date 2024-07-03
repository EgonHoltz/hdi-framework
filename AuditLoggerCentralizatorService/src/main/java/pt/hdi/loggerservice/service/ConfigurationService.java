package pt.hdi.loggerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.loggerservice.model.Application;
import pt.hdi.loggerservice.model.Configuration;
import pt.hdi.loggerservice.model.DocumentData;
import pt.hdi.loggerservice.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository confRep;	

	public Configuration getConfigurationByDocApp(DocumentData doc, Application app){
		return confRep.findByDocumentApplication(doc.getId(), app.getId());
	}

}
