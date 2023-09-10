package pt.hdi.restservice.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:8080") // Add the origin of your Vue.js app
        .allowedMethods("GET", "POST", "PUT", "DELETE"); // Add the HTTP methods you want to allow
    }
    
}
