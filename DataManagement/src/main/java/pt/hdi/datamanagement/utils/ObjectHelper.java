package pt.hdi.datamanagement.utils;

public class ObjectHelper {
    public static String getCamelFieldName(String fieldName) {
        StringBuilder camelFieldName = new StringBuilder();
        String[] words = fieldName.split("\\s+");
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                camelFieldName.append(word.toLowerCase());
            } else {
                camelFieldName.append(Character.toUpperCase(word.charAt(0)))
                              .append(word.substring(1).toLowerCase());
            }
        }
        
        return camelFieldName.toString();
    }
}
