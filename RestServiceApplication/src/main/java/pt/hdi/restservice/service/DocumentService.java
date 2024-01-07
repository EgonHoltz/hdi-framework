package pt.hdi.restservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import pt.hdi.restservice.model.DocumentData;
import pt.hdi.restservice.model.Structure;
import pt.hdi.restservice.repository.DocumentRepository;

@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository docRep;

    public boolean editStructureField(String documentId, Structure updatedStructure) {
        DocumentData document = docRep.findById(documentId).orElse(null);
        String fieldName = updatedStructure.getFieldName();

        if (document != null) {
            List<Structure> structures = document.getStructures();

            for (int i = 0; i < structures.size(); i++) {
                Structure existingStructure = structures.get(i);
                if (existingStructure.getFieldName().equals(fieldName)) {
                    document.removeStructureById(i);
                    document.addStructure(updatedStructure);                    
                    docRep.save(document);
                    return true; 
                }
            }
        }
        return false;
    }

        public boolean addStructure(String documentId, Structure updatedStructure) {
        DocumentData document = docRep.findById(documentId).orElse(null);

        if (document != null) {
            document.addStructure(updatedStructure);
            docRep.save(document);
            return true;
        }
        return false;
    }

}
