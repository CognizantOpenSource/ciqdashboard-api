package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CIQDashboardRepository extends MongoRepository<CIQDashboard, String> {
    void deleteByIdIn(List<String> ids);

    List<CIQDashboard> findByProjectName(String projectName);

    List<CIQDashboard> findByProjectNameIn(List<String> projectNames);

    void deleteByProjectName(String projectName);

    Optional<CIQDashboard> findByNameIgnoreCase(String name);
}
