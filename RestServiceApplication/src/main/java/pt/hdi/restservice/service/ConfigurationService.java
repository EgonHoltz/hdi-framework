package pt.hdi.restservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import pt.hdi.restservice.Utils.ApplicationEnums.FLOW_DIRECTION;
import pt.hdi.restservice.bean.ConfigurationGrpcBean;
import pt.hdi.restservice.bean.ConfigurationMQBean;
import pt.hdi.restservice.bean.ConfigurationSFTPBean;
import pt.hdi.restservice.bean.ConfigurationSFTPSchedulerBean;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.model.Configuration;
import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.MQConfig;
import pt.hdi.restservice.model.SFTPConfig;
import pt.hdi.restservice.model.SftpFileSchedulerConfig;
import pt.hdi.restservice.repository.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository confRep;
	
	public List<ConfigurationMQBean> getAllMqConfigs(){
		List<Configuration> allConfig = confRep.findAll();
		List<ConfigurationMQBean> mqConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : allConfig) {
			if (cfg.getMqConfig() != null) {
				mqConfig.add(new ConfigurationMQBean(cfg));
			}
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
		if (conf != null) {
			return new ConfigurationMQBean(conf);
		}
		return null;
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
		if (configs == null || CollectionUtils.isEmpty(configs)) {
			return new ArrayList<>();
		}
		for (Configuration cfg : configs) {
			if (cfg.getMqConfig() != null){
				mqConfig.add(new ConfigurationMQBean(cfg));
			}
		}
		return mqConfig;
	}

    public ConfigurationSFTPBean getByFileName(String fileName) {
		Configuration conf = confRep.findBySftpConfigSftpFileName(fileName);
		if (conf != null) {
			return new ConfigurationSFTPBean(conf);
		}
		return null;
    }

	public List<ConfigurationSFTPBean> getAllSftpConfigs() {
		List<Configuration> allConfig = confRep.findAll();
		List<ConfigurationSFTPBean> sftpConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : allConfig) {
			if (cfg.getSftpConfig() != null){
				sftpConfig.add(new ConfigurationSFTPBean(cfg));
			}
		}
		return sftpConfig;
	}
	
	public List<ConfigurationSFTPSchedulerBean> getAllSftpSchedulerConfigs() {
		List<Configuration> allConfig = confRep.findAll();
		List<ConfigurationSFTPSchedulerBean> sftpConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : allConfig) {
			if (cfg.getSftpConfig() != null){
				sftpConfig.add(new ConfigurationSFTPSchedulerBean(cfg));
			}
		}
		return sftpConfig;
	}

	public ConfigurationSFTPSchedulerBean getSftpSchedulerDeltaId(String configId){
		Optional<Configuration> config = confRep.findById(configId);
		if (config.isPresent()){
			return new ConfigurationSFTPSchedulerBean(config.get());
		} else {
			return null;
		}
	}

	public ConfigurationSFTPSchedulerBean updateSftpSchedulerDeltaId(String configId, String lastDocId){
		Optional<Configuration> config = confRep.findById(configId);
		if (config.isPresent()){
			Configuration foundConfig = config.get();
			foundConfig.getSftpSchedulerConfig().setLastObjectId(lastDocId);
			confRep.save(foundConfig);
			return new ConfigurationSFTPSchedulerBean(foundConfig);
		} else {
			return null;
		}
	}

    public ConfigurationGrpcBean getByGrpcClientId(String clientId) {
		Configuration conf = confRep.findByGrpcConfigClientId(clientId);
		if (conf != null) {
			return new ConfigurationGrpcBean(conf);
		}
		return null;
    }

	public List<ConfigurationGrpcBean> getAllGrpcConfigs() {
		List<Configuration> allConfig = confRep.findAll();
		List<ConfigurationGrpcBean> grpcConfig = new ArrayList<>();
		// What if there is no config?
		for (Configuration cfg : allConfig) {
			if (cfg.getGrpcConfig() != null){
				grpcConfig.add(new ConfigurationGrpcBean(cfg));
			}
		}
		return grpcConfig;
	}


	public List<DocumentData> getDocumentDataByApplicationWithSftp(String applicationId){
		
		List<Configuration> configs = confRep.findConfigurationsByApplication(applicationId);

		List<DocumentData> docs = new ArrayList<>(); 
		if (configs == null || CollectionUtils.isEmpty(configs) ){
			return docs;
		}
		// for all configurations by application, find which
		// one has SFTP with sending configuration
		for (Configuration configuration : configs) {
			if (configuration.getSftpConfig() != null){
				for (SFTPConfig sftp : configuration.getSftpConfig()){
					if (FLOW_DIRECTION.SEND.equals(sftp.getDirection())){
						docs.add(configuration.getDocumentData());
					}
				}
			}
		}
		return docs;
	}

	public void upsertSftpSendFileConfig(Configuration conf, SftpFileSchedulerConfig newSftpSchedulerConf){
		conf.setSftpSchedulerConfig(newSftpSchedulerConf);

		confRep.save(conf);
	}

	public void updateMqOnConfiguration(Configuration conf, MQConfig mc){
		for (MQConfig confMq : conf.getMqConfig()){
			if (confMq.getDirection().equals(mc.getDirection())){
				confMq.setActive(mc.getActive());
				confMq.setHasAck(mc.getHasAck());
				confMq.setMqName(mc.getMqName());
				confMq.setUser(mc.getUser());
				confMq.setPassword(mc.getPassword());
			}
		}
		confRep.save(conf);
	}

    public Configuration getConfigurationById(String configId) {
		Optional<Configuration> conf = confRep.findById(configId);
		if (conf.isPresent()){
			return conf.get();
		}
		return null;
    }

	// TODO: gRPC and SFTP

}
