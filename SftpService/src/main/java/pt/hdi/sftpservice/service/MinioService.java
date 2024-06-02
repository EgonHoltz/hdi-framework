package pt.hdi.sftpservice.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    public InputStream getFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        } catch (MinioException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while fetching the file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while fetching the file: " + e.getMessage());
        }
    }
}
