package pt.hdi.filegenerator.thread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import pt.hdi.filegenerator.Utils.FileUtils;
import pt.hdi.filegenerator.Utils.ApplicationEnums.SEND_FILE_DATA_MODE;
import pt.hdi.filegenerator.model.Configuration;
import pt.hdi.filegenerator.model.FileAuditLogger;
import pt.hdi.filegenerator.service.AuditLoggerService;
import pt.hdi.filegenerator.service.ConfigurationService;
import pt.hdi.filegenerator.service.MinioService;

public class FileGeneration implements Runnable {

    private final Configuration config;
    private final MongoTemplate mongoTemplate;
    private final String filePath;
    private final ConfigurationService confSvc;
    private final MinioService minioSvc;
    private final AuditLoggerService auditSvc;
    
    public FileGeneration(Configuration config, MongoTemplate mongoTemplate, String filePath, ConfigurationService confSvc,
            MinioService minioSvc,AuditLoggerService auditSvc){
        this.config = config;
        this.mongoTemplate = mongoTemplate;
        this.filePath = filePath;
        this.confSvc = confSvc;
        this.minioSvc = minioSvc;
        this.auditSvc = auditSvc;
    }

    @Override
    public void run() {
        String documentName = config.getDocumentDataName().replaceAll("\\s", "").toLowerCase();
        LocalDateTime creationDate = LocalDateTime.now(); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String formattedDate = creationDate.format(formatter);
        String fileName = "output_" + config.getApplicationAbreviation() + "_" + config.getDocumentDataName() +"_"+ formattedDate +".json";
        long lineCount = 0;
        String fullPath = filePath+"/"+fileName;
        System.out.println("[" + getTaskKey(config) + "] "+ "File will be generated on the path with name: " + fullPath);

        List<Object> documents = new ArrayList<>();
        //Threat the DELTA or FULL File
        if (SEND_FILE_DATA_MODE.FULL.equals(config.getSftpSchedulerConfig().getDataMode())){
            System.out.println("[" + getTaskKey(config) + "] "+ "Threating the full generation file");
            documents = mongoTemplate.findAll(Object.class, documentName);
        } else {
            String lastDocId = confSvc.getLastDocumentIdForDeltaByConfiguration(config.getId());
            System.out.println("[" + getTaskKey(config) + "] "+ "Threating the delta generation file by last ID: " + lastDocId);
            documents = fetchNewData(documentName, lastDocId);
        }

        // Generate the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {

            writer.write("000_HDI_" + formattedDate + "_" + config.getApplicationAbreviation() + "_" + config.getDocumentDataName() + "_000\n");

            for (Object doc : documents) {
                String line = doc.toString() + "\n";
                if (doc instanceof Document){
                    Document document = (Document) doc;
                    line = document.toJson()+ "\n";
                }
                writer.write(line);
                lineCount++;
            }
            writer.write("999_" + lineCount + "_999\n");
            System.out.println("[" + getTaskKey(config) + "] "+"File generated with " + lineCount + " lines.");

            Thread.sleep(1000);
            
            if (SEND_FILE_DATA_MODE.DELTA.equals(config.getSftpSchedulerConfig().getDataMode())){
                if (documents.size() > 0) {
                    Document lastDocument = (Document) documents.get(documents.size() - 1);  
                    ObjectId lastId = lastDocument.getObjectId("_id");
                    System.out.println("[" + getTaskKey(config) + "] "+ "Updating the last ObjectID: " +lastId);
                    confSvc.updateLastDocumentIdForDeltaByConfiguration(lastId.toString(), config.getId());
                } else {
                    System.out.println("[" + getTaskKey(config) + "] "+ "No updates on last ID, files is zero");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        // Send generated file to Minio
        try {
            boolean fileSent;
            fileSent = sendFileToNio(fullPath);
            if (fileSent){
                System.out.println("[" + getTaskKey(config) + "] "+"File sent to Minio with success!");
            } else {
                System.out.println("[" + getTaskKey(config) + "] "+"Something went wrong when while file was sent to Minio");
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Add log of file generation to DB by RestService.
        String minioPath = minioSvc.getBucket() + "/" + fileName;
        Instant iCreationDate = creationDate.atZone(ZoneId.systemDefault()).toInstant(); 
        FileAuditLogger audit = new FileAuditLogger(config.getId(), fileName, minioPath, Date.from(iCreationDate), lineCount);
        ResponseEntity res = auditSvc.addFileSendSftpEntry(audit);
        if (!HttpStatus.CREATED.equals(res.getStatusCode())){
            System.out.println("[" + getTaskKey(config) + "] "+"Something went wrong on centralizator server to send creation information.");            
        } else {
            System.out.println("[" + getTaskKey(config) + "] "+"Information Added with success.");            
        }
        
        System.out.println("[" + getTaskKey(config) + "] "+"Process finished");
    }

    private String getTaskKey(Configuration config) {
        return config.getDocumentDataName() + "-" + config.getApplicationAbreviation();
    }

    private boolean sendFileToNio(String path) throws IOException{
        System.out.println("[" + getTaskKey(config) + "] "+ "Send the file to Nio...");
        return minioSvc.uploadFile(FileUtils.fileToMultipartFile(path));
    }

    private List<Object> fetchNewData(String collectionName, String lastProcessedId) {
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);
        List<Object> newDocuments = new ArrayList<>();

        MongoCursor<Document> cursor;
        if (lastProcessedId != null) {
            cursor = collection.find(new Document("_id", new Document("$gt", new ObjectId(lastProcessedId)))).iterator();
        } else {
            cursor = collection.find().iterator();
        }

        while (cursor.hasNext()) {
            newDocuments.add(cursor.next());
        }
        cursor.close();

        return newDocuments;
    }
    
}
