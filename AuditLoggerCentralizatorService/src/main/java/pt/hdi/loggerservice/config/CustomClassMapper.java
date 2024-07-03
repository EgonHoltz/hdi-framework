package pt.hdi.loggerservice.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.ClassMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomClassMapper implements ClassMapper {
    private final Map<Pattern, Class<?>> patternClassMapping = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setPatternClassMapping(Map<String, Class<?>> patternClassMapping) {
        for (Map.Entry<String, Class<?>> entry : patternClassMapping.entrySet()) {
            this.patternClassMapping.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public void fromClass(Class<?> clazz, MessageProperties properties) {
        String className = clazz.getName();
        properties.getHeaders().put("__TypeId__", className);
    }

    @Override
    public Class<?> toClass(MessageProperties properties) {
        String typeId = (String) properties.getHeaders().get("__TypeId__");
        if (typeId != null) {
            for (Map.Entry<Pattern, Class<?>> entry : patternClassMapping.entrySet()) {
                if (entry.getKey().matcher(typeId).matches()) {
                    return entry.getValue();
                }
            }
            try {
                return Class.forName(typeId);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Cannot find class for type ID [" + typeId + "]");
            }
        }
        return null;
    }
}
