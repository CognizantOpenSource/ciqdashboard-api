package com.cognizant.idashboardapi.repos.impl;

import com.cognizant.idashboardapi.models.ProjectMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectMappingRepositoryImpl {
    @Autowired
    MongoTemplate template;

    public void updateWithTeams(String teamId, List<String> projectIds) {
        Query query = Query.query(Criteria.where("_id").in(projectIds));
        Update update = new Update();
        update.addToSet("teamIds", teamId);
        template.updateMulti(query, update, ProjectMapping.class);
    }

    public void removeWithTeams(String projectId, String teamId) {
        Query query = Query.query(Criteria.where("_id").is(projectId));
        Update update = new Update();
        update.pull("teamIds", teamId);
        template.updateFirst(query, update, ProjectMapping.class);
    }
}
