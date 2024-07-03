package pt.hdi.loggerservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.loggerservice.model.AuditLogger;
import pt.hdi.loggerservice.repository.AuditLoggerRepository;

@Service
public class RabbitAuditLoggerService {
    @Autowired
    private AuditLoggerService auditSvc;

    @RabbitListener(queues = "logger")
    public void receiveMessage(AuditLogger auditLogger) {
        System.out.println("Received message: " + auditLogger);
        auditSvc.addNewAuditLogger(auditLogger);
    }
}
