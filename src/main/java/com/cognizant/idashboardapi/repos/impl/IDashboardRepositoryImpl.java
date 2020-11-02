package com.cognizant.idashboardapi.repos.impl;

import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class IDashboardRepositoryImpl {
    @Autowired
    MongoTemplate template;
    @Autowired
    UserValidationService userValidationService;

    public Optional<IDashboard> findById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        IDashboard dashboard = template.findOne(query, IDashboard.class);
        return Optional.ofNullable(dashboard);
    }

    public List<IDashboard> getByProjectName(String projectName) {
        Criteria criteria = Criteria.where("projectName").is(projectName);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, IDashboard.class);
    }

    public List<IDashboard> findAll() {
        Criteria criteria = new Criteria();
        criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, IDashboard.class);
    }

    public void deleteByIds(List<String> ids) {
        Criteria criteria = Criteria.where("_id").in(ids);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        template.remove(query, IDashboard.class);
    }

    private Criteria[] getCriteria() {
        Criteria[] criteriaArray = new Criteria[2];
        criteriaArray[0] = Criteria.where("openAccess").in(false, null).and("createdUser").is(userValidationService.getCurrentUserEmailId());
        criteriaArray[1] = Criteria.where("openAccess").is(true).and("projectName").in(userValidationService.getCurrentUserProjectNames());
        return criteriaArray;
    }
}
