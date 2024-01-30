package pt.hdi.restservice.Utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ObjectHelper {
       // Helper method to get the names of null properties in an object
       public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        List<String> nullProperties = new ArrayList();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                nullProperties.add(pd.getName());
            }
        }

        return nullProperties.toArray(new String[0]);
    }

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
