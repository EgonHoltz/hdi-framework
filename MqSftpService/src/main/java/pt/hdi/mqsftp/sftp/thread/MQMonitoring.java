package pt.hdi.mqsftp.sftp.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
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
	
	private MongoTemplate mt;
	
    public MQMonitoring(ApplicationContext ctx) {
        this.ctx = ctx;
    }
	
	@Override
	public void run() {

		confService = ctx.getBean(ConfigurationService.class);
		mt = ctx.getBean(MongoTemplate.class);
		
		try {
			MQConnectionBean conConf = new MQConnectionBean("localhost", "quser", "qpass");
			// It means, the service is starting on the first time, so look at all
			// MQ Queue that are already instanciated once
			List<String> rabbitQueuesNames = confService.getAllConfigs().stream()
					.flatMap(conf -> Optional.ofNullable(conf.getMqConfig()).orElse(Collections.emptyList()).stream())
					.filter(mq -> mq.hasStarted())
					.map(MQConfig::getMqName)
					.collect(Collectors.toList());
			rabbitQueuesNames.stream().forEach(queueNames -> {
				Optional.ofNullable(queueNames).ifPresent(queue -> {
					Executor exec = Executors.newSingleThreadExecutor();
					exec.execute(new ReadMQMessage(queue, conConf));
					System.out.println("Started queue: " + queue);
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
				
				List<MQConfig> rabbitQueuesNotStarted = mqNotStarted.stream()
						.flatMap(conf -> Optional.ofNullable(conf.getMqConfig()).orElse(Collections.emptyList()).stream())
						.filter(mq -> !mq.hasStarted())
						.collect(Collectors.toList());
				rabbitQueuesNotStarted.stream().forEach(queueNames -> {
					Optional.ofNullable(queueNames).ifPresent(queue -> {
						Executor exec = Executors.newSingleThreadExecutor();
						exec.execute(new ReadMQMessage(queue.getMqName(), conConf));
						findQueueAndDeclareStarted(confService.getByRqName(queue.getMqName()),queue.getMqName());
						System.out.println("Added new queue and started: " + queue);
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

	private void findQueueAndDeclareStarted(Configuration conf, String qName) {

		List<MQConfig> newMqList = new ArrayList<MQConfig>();
		for (MQConfig mqconf : conf.getMqConfig()) {
			if (mqconf.getMqName().equals(qName)) {
				mqconf.setStarted(true);
			}
			newMqList.add(mqconf);
		}
		conf.removeAllMqConfig();
		newMqList.stream().forEach(n -> conf.addMqConfig(n));
		confService.saveConfiguration(conf);
	}
	
}
