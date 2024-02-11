package pt.hdi.mqsftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class MqSftpServiceApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(MqSftpServiceApplication.class, args);
		System.out.println("Application started");
		
    	ResourceLoader resourceLoader = (ResourceLoader) ctx;
        Resource resource = resourceLoader.getResource("classpath:application.properties");
        Properties props = new Properties();
        try (InputStream is = resource.getInputStream()) {
            props.load(is);
        } catch (IOException e) {
            // handle error
        }
        System.out.println(props.toString());

	}
}
