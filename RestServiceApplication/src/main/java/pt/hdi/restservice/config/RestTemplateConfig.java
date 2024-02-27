package pt.hdi.restservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {


    @Value("${rabbitmq.api.username}")
    private String rabbitUser;

    @Value("${rabbitmq.api.password}")
    private String rabbitPass;
    

    @Bean
    public RestTemplate restTemplateSimple() {
        return new RestTemplate();
    }
    @Bean
    public RestTemplate restTemplateWithRabbitAuth(RestTemplateBuilder builder) {
        return builder
                .basicAuthentication(rabbitUser, rabbitPass)
                .build();
    }
}
