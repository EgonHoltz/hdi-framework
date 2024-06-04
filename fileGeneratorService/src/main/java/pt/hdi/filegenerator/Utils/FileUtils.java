package pt.hdi.filegenerator.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

     public static MultipartFile fileToMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(file.getName(), file.getName(), "text/plain", input);
    }
}
