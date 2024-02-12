package pt.hdi.mqservice.Utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFieldValidator {
        private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validateFields(String jsonMessage, List<String> expectedFields) {
        Set<String> fieldsSet = new HashSet<>(expectedFields);
        try {
            JsonNode rootNode = objectMapper.readTree(jsonMessage);
            if (rootNode.isObject()) {
                rootNode.fieldNames().forEachRemaining(fieldName -> fieldsSet.remove(fieldName));
                // If fieldsSet is empty, all expected fields were in the JSON
                return fieldsSet.isEmpty();
            }
            return false;
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return false;
        }
    }
}
