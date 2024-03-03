package pt.hdi.sftpservice.utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.hdi.sftpservice.model.Structure;
import pt.hdi.sftpservice.utils.ObjectHelper;

public class JsonFieldValidator {
   private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validate(String json, List<Structure> structures) {
        try {
            Map<String, Object> dataMap = objectMapper.readValue(json, Map.class);
            for (Structure structure : structures) {
                String fieldName = ObjectHelper.getCamelFieldName(structure.getFieldName());
                Object value = dataMap.get(fieldName);

                // Check if mandatory field is present
                if (structure.isMandatory() && value == null) {
                    System.out.println("Missing mandatory field: " + fieldName);
                    return false;
                }

                // Check type
                if (!"String".equals(structure.getType()) && !(value instanceof String)) {
                    System.out.println("Invalid type for field: " + fieldName);
                    return false;
                }

                // Check regular expression
                if (!structure.getRegExp().isEmpty() && !Pattern.matches(structure.getRegExp(), (String) value)) {
                    System.out.println("Field does not match the regular expression: " + fieldName);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
