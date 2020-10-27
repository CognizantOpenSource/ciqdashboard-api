package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.IDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IDashboardRepository extends MongoRepository<IDashboard, String> {
    void deleteByIdIn(List<String> ids);

    List<IDashboard> findByProjectName(String projectName);

    List<IDashboard> findByProjectNameIn(List<String> projectNames);

    void deleteByProjectName(String projectName);
}
