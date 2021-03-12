package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.ProjectMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectMappingRepository extends MongoRepository<ProjectMapping, String> {
    List<ProjectMapping> findByUserIdsContaining(String userId);

    List<ProjectMapping> findByUserIdsContainingOrTeamIdsIn(String userId, List<String> teamIds);

    List<ProjectMapping> findByOwnerId(String userId);

    List<ProjectMapping> findByTeamIdsContaining(String teamName);
}
