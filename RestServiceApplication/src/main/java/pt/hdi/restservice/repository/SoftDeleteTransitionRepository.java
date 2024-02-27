package pt.hdi.restservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.hdi.restservice.model.SoftDeleteTransition;
import java.util.List;


public interface SoftDeleteTransitionRepository extends MongoRepository<SoftDeleteTransition,String> {
    List<SoftDeleteTransition> findByCollectionId(String collectionId);
    SoftDeleteTransition findByCollectionIdAndDataId(String collectionId, String DataId);
        
}
