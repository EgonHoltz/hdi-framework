package pt.hdi.filegenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.filegenerator.model.Configuration;
import pt.hdi.filegenerator.service.FileGenerationSchedulerService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    
    @Autowired
    public FileGenerationSchedulerService fgss;

    @PostMapping("/add")
    public ResponseEntity<String> addSchedulerConfig(@RequestBody Configuration config){
        fgss.scheduleFileGeneration(config);
        return ResponseEntity.ok("ADDED");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateSchedulerConfig(@RequestBody Configuration config){
        fgss.updateScheduledTask(config);
        return ResponseEntity.ok("UPDATED");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSchedulerConfig(@RequestBody Configuration config){
        fgss.cancelScheduledTasks(config);
        return ResponseEntity.ok("CANCELLED");
    }

}
