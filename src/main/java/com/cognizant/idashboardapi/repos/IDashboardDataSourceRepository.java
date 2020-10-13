package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.IDashboardDataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IDashboardDataSourceRepository extends MongoRepository<IDashboardDataSource, String> {
    void deleteByIdIn(List<String> ids);

    Optional<IDashboardDataSource> findByName(String name);
}
