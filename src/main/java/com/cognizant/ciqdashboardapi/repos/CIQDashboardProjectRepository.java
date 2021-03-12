package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.CIQDashboardProject;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CIQDashboardProjectRepository extends MongoRepository<CIQDashboardProject, String> {
    Optional<CIQDashboardProject> findByName(String name);

    Optional<CIQDashboardProject> findFirstByNameAndIdNot(String name, String id);

    @Query(value = "{_id: { $in: ?0 } })")
    List<CIQDashboardProject> findByIds(List<String> ids, Sort sort);

    Optional<CIQDashboardProject> findByNameIgnoreCase(String name);
}
