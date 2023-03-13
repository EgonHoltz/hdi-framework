package pt.hdi.mqsftp.sftp.thread;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import pt.hdi.mqsftp.sftp.bean.MQConnectionBean;
import pt.hdi.mqsftp.sftp.model.Configuration;
import pt.hdi.mqsftp.sftp.model.MQConfig;
import pt.hdi.mqsftp.sftp.service.ConfigurationService;

@Component
public class MQMonitoring implements Runnable {

	private ConfigurationService confService;
	
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private MongoTemplate mt;
	
    public MQMonitoring(ApplicationContext ctx) {
        this.ctx = ctx;
    }
	
	@Override
	public void run() {

		confService = ctx.getBean(ConfigurationService.class);
		
		try {
			MQConnectionBean conConf = new MQConnectionBean("localhost", "quser", "qpass");
			// It means, the service is starting on the first time, so look at all
			// MQ Queue that are already instanciated once
			List<String> rabbitQueuesNames = confService.getAllConfigs().stream()
					.flatMap(conf -> conf.getMqConfig().stream())
					.filter(mq -> mq.getDirection() == "recv" && mq.hasStarted())
					.map(MQConfig::getMqName)
					.collect(Collectors.toList());
			rabbitQueuesNames.stream().forEach(queueNames -> {
				Optional.ofNullable(queueNames).ifPresent(queue -> {
					Executor exec = Executors.newSingleThreadExecutor();
					exec.execute(new ReadMQMessage(queue, conConf));
				});
			});

			
			while(true) {
				//Wait 10 seconds
				Thread.sleep(10000);

				// This will allow to create queue instantly and does not need to change the app
			
				List<Configuration> mqNotStarted = confService.getAllConfigurationWithMQAndNotStarted();
				
				if(mqNotStarted.isEmpty()) {
					continue;
				}
				
				List<String> rabbitQueuesNotStartedNames = mqNotStarted.stream()
						.flatMap(conf -> conf.getMqConfig().stream())
						.filter(mq -> mq.getDirection() == "recv" && !mq.hasStarted())
						.map(MQConfig::getMqName)
						.collect(Collectors.toList());
				rabbitQueuesNotStartedNames.stream().forEach(queueNames -> {
					Optional.ofNullable(queueNames).ifPresent(queue -> {
						Executor exec = Executors.newSingleThreadExecutor();
						exec.execute(new ReadMQMessage(queue, conConf));
						findQueueAndDeclareStarted(mqNotStarted);
					});
				});


			}	
		} catch (Exception e) {
			System.err.println("Problems with IO: "+e.getStackTrace());
			e.printStackTrace();
		} finally {
			Executor exec = Executors.newFixedThreadPool(1);
			exec.execute(new MQMonitoring(ctx));
		}
	}

	private void findQueueAndDeclareStarted(List<Configuration> mqNotStarted) {
		mqNotStarted.stream().forEach(conf -> {
			Query q = new Query();
			q.addCriteria(Criteria.where("_id").is(conf.getId()).and("mqConfig.started").ne(true));
			
			Update u = new Update();
			u.set("mqConfig.$.started", true);
			
			mt.updateMulti(q, u, Configuration.class);
		});
	}
	
}
