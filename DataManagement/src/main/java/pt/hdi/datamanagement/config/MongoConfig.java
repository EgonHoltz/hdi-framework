package pt.hdi.datamanagement.config;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer()
    {
        return builder -> builder.serializerByType(ObjectId.class,new ToStringSerializer());
    }
    
    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(new ObjectIdToStringConverter()));
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}
