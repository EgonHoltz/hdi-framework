package pt.hdi.sftpservice.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static byte[] compressFile(String sourceFilePath) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String zipFilePath ="";
        String zipPathAndFileName = "";

        try {
            ZipOutputStream zos = new ZipOutputStream(baos);

            // Create a zip entry for the source file
            Path srcPath = Paths.get(sourceFilePath);
            String fileName = srcPath.getFileName().toString();
            ZipEntry ze = new ZipEntry(fileName);
            zos.putNextEntry(ze);

            // Create file input stream for the source file
            FileInputStream in = new FileInputStream(sourceFilePath);

            // Read from the source file and write to the zip output stream
            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            // Close the current entry and the zip output stream
            in.close();
            zos.closeEntry();
            zos.close();

            // Convert the ByteArrayOutputStream to byte array
            return baos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
