package pt.hdi.loggerservice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.hdi.loggerservice.service.RabbitAuditLoggerService;

@Configuration
public class RabbitMQConsumerConfig {
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        CustomClassMapper classMapper = new CustomClassMapper();

        Map<String, Class<?>> patternClassMapping = new HashMap<>();
        patternClassMapping.put(".*\\.AuditLogger$", pt.hdi.loggerservice.model.AuditLogger.class);
        patternClassMapping.put(".*\\.FileAuditLogger$", pt.hdi.loggerservice.model.FileAuditLogger.class);

        classMapper.setPatternClassMapping(patternClassMapping);
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory,
                                                            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("logger");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RabbitAuditLoggerService receiver) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        listenerAdapter.setMessageConverter(jackson2JsonMessageConverter());
        return listenerAdapter;
    }
}
