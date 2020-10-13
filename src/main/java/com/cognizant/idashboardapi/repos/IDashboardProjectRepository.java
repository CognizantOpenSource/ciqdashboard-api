package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.IDashboardProject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IDashboardProjectRepository extends MongoRepository<IDashboardProject, String> {
    Optional<IDashboardProject> findByName(String name);
    Optional<IDashboardProject> findFirstByNameAndIdNot(String name, String id);
    @Query("{_id: { $in: ?0 } })")
    List<IDashboardProject> findByIds(List<String> ids);
}
