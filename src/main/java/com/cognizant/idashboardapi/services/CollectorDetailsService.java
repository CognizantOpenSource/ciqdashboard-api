package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceExistsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.CollectorDetails;
import com.cognizant.idashboardapi.models.DatabaseDetailsComponent;
import com.cognizant.idashboardapi.repos.CollectorDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CollectorDetailsService {

    private static final String MODEL_NAME = "CollectorDetails";

    @Autowired
    CollectorDetailsRepository repository;
    @Autowired
    DatabaseDetailsComponent databaseDetailsComponent;
    @Autowired
    MongoTemplate mongoTemplate;

    public List<CollectorDetails> getAll() {
        return repository.findAll();
    }

    public Optional<CollectorDetails> get(String id) {
        return repository.findById(id);
    }

    public Optional<CollectorDetails> getByName(String name) {
        return repository.findByName(name);
    }

    public CollectorDetails assertOrGet(String id) {
        Optional<CollectorDetails> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException(MODEL_NAME, "id", id);
        }
    }

    public CollectorDetails assertOrGetByName(String name) {
        Optional<CollectorDetails> optional = getByName(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException(MODEL_NAME, "name", name);
        }
    }

    public CollectorDetails add(CollectorDetails collectorDetails) {
        assertInsert(collectorDetails);
        return repository.insert(collectorDetails);
    }

    public CollectorDetails update(CollectorDetails collectorDetails) {
        assertOrGet(collectorDetails.getId());
        assertCollectionName(collectorDetails.getCollectionName());
        try {
            return repository.save(collectorDetails);
        } catch (DuplicateKeyException exception){
            throw new ResourceExistsException(MODEL_NAME, "name", collectorDetails.getName());
        }
    }

    public void deleteById(String id) {
        assertOrGet(id);
        repository.deleteById(id);
    }

    private void assertInsert(CollectorDetails collectorDetails) {
        if (!StringUtils.isEmpty(collectorDetails.getId())) {
            throw new InvalidDetailsException("id should be empty/null");
        }
        Optional<CollectorDetails> optional = getByName(collectorDetails.getName());
        if (optional.isPresent()) {
            throw new ResourceExistsException(MODEL_NAME, "name", collectorDetails.getName());
        }
        assertCollectionName(collectorDetails.getCollectionName());
    }

    private void assertCollectionName(String name){
        log.info("Collection Name: {}", name);
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        log.info("Collections List: {}", collectionNames);
        log.info("Collection contain : {}", collectionNames.contains(name));
        if (!collectionNames.contains(name)){
            throw new InvalidDetailsException("Collection Name not Found in Database with name : "+name);
        }
    }
}
