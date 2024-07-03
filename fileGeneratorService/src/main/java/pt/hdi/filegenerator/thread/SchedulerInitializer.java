package pt.hdi.filegenerator.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import pt.hdi.filegenerator.model.Configuration;
import pt.hdi.filegenerator.model.SftpFileSchedulerConfig;
import pt.hdi.filegenerator.service.FileGenerationSchedulerService;
import pt.hdi.filegenerator.service.ConfigurationService;

@Component
public class SchedulerInitializer {

    @Autowired
    private FileGenerationSchedulerService schedulerService;

    @Autowired
    private ConfigurationService rmSrvc;

    @PostConstruct
    public void initializeSchedules() {
        List<Configuration> configs = rmSrvc.getAllConfigsWithSendSFTP();

        for (Configuration config : configs) {
            schedulerService.scheduleFileGeneration(config);
        }
    }

}
