package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.CIQDashboardDataSource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CIQDashboardDataSourceRepository extends MongoRepository<CIQDashboardDataSource, String> {
    void deleteByIdIn(List<String> ids);

    Optional<CIQDashboardDataSource> findByName(String name);

    Optional<CIQDashboardDataSource> findByNameIgnoreCase(String name);
}
