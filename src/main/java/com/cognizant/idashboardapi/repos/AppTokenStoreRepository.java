package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.AppTokenStore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppTokenStoreRepository extends MongoRepository<AppTokenStore, String> {
    Optional<AppTokenStore> findByRefId(String refId);

    void deleteByRefId(String id);
}
