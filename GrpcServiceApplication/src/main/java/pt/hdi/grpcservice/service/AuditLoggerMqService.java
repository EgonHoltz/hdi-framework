package pt.hdi.grpcservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.hdi.grpcservice.model.AuditLogger;

@Service
public class AuditLoggerMqService {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName = "logger";

    @Autowired
    public AuditLoggerMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAuditLog(AuditLogger auditLogger) {
        rabbitTemplate.convertAndSend(queueName, auditLogger);
        System.out.println("AuditLogger sent to queue: " + queueName);
    }
}
