package pt.hdi.filegenerator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import pt.hdi.filegenerator.model.Configuration;
import pt.hdi.filegenerator.model.SftpFileSchedulerConfig;
import pt.hdi.filegenerator.thread.FileGeneration;

@Service
public class FileGenerationSchedulerService {
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${spring.temp.filepath.generation}")
    private String filePath;

    @Autowired
    private ConfigurationService cfgSvc;

    @Autowired
    private MinioService minioSvc;

    @Autowired
    private AuditLoggerService auditSvc;

    public FileGenerationSchedulerService() {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        ((ThreadPoolTaskScheduler) this.taskScheduler).initialize();
    }

    public void scheduleFileGeneration(Configuration config) {
        if (config.getSftpSchedulerConfig() == null){
            System.out.println("No cron for configuration: " + config.getApplicationAbreviation() + config.getDocumentDataName());
            return;
        }
        Runnable task = () -> generateFile(config);
        CronTrigger cronTrigger = new CronTrigger(config.getSftpSchedulerConfig().getCron());
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(task, cronTrigger);
        // Create unique key for each doc+app
        String taskKey = getTaskKey(config);
        scheduledTasks.put(taskKey, scheduledTask);
        System.out.println("Registered cron: " + config.getSftpSchedulerConfig().getCron() + " with key: " + taskKey);
    }

    private String getTaskKey(Configuration config) {
        return config.getDocumentDataName() + "-" + config.getApplicationAbreviation();
    }

    @Async
    @Retryable(
        value = {Exception.class},
        maxAttempts = 5,
        backoff = @Backoff(delay = 2000)
    )
    private void generateFile(Configuration config) {
        try {
            System.out.println("Generating file for application: " + config.getApplicationAbreviation() + ", document: " + config.getDocumentDataName());
            Runnable thread = new FileGeneration(config, mongoTemplate, filePath, cfgSvc, minioSvc, auditSvc);
            thread.run();
        } catch (Exception e) {
            System.err.println("Failed to generate file for application: " + config.getApplicationAbreviation() + ", document: " + config.getDocumentDataName());
        }
    }

    public void cancelScheduledTasks(Configuration config) {
        String taskKey = getTaskKey(config);
        ScheduledFuture<?> scheduleTask = scheduledTasks.get(taskKey);
        if (scheduleTask != null){
            scheduleTask.cancel(false);
            scheduledTasks.remove(taskKey);
        }
    }

    public void updateScheduledTask(Configuration config){
        cancelScheduledTasks(config);
        scheduleFileGeneration(config);
    }

}
