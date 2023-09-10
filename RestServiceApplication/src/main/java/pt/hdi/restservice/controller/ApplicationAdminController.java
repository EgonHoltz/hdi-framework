package pt.hdi.restservice.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.hdi.restservice.Utils.ObjectHelper;
import pt.hdi.restservice.model.Application;
import pt.hdi.restservice.repository.ApplicationRepository;


@RestController
@RequestMapping("/application")
public class ApplicationAdminController {

    @Autowired
    ApplicationRepository appRep;

    @GetMapping("/")
    public List<Application> getAllApplications(){
            System.out.println("Called getAllApplications");
            return appRep.findAll();
    }

    @PostMapping("/")
    public ResponseEntity createApplication(Application app){
        System.out.println("Called createApplications");
        if (app == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Application newApp = appRep.save(app);
        if (newApp != null){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}")
    public ResponseEntity changeApplication(@PathVariable String id, @RequestBody Application app){
        if (id == null || app == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Application> appFound = appRep.findById(id);
        
        if (!appFound.isPresent() ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BeanUtils.copyProperties(appFound, app, ObjectHelper.getNullPropertyNames(appFound));

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
