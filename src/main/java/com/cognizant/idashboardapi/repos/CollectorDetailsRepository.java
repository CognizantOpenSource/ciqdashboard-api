package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.CollectorDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CollectorDetailsRepository extends MongoRepository<CollectorDetails, String> {
    Optional<CollectorDetails> findByName(String name);
}
