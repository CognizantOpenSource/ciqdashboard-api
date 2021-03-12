package com.cognizant.ciqdashboardapi.repos.impl;

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CIQDashboardRepositoryImpl {
    @Autowired
    MongoTemplate template;
    @Autowired
    UserValidationService userValidationService;

    public Optional<CIQDashboard> findById(String id) {
        Criteria criteria = Criteria.where("_id").is(id);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        CIQDashboard dashboard = template.findOne(query, CIQDashboard.class);
        return Optional.ofNullable(dashboard);
    }

    public List<CIQDashboard> getByProjectName(String projectName) {
        Criteria criteria = Criteria.where("projectName").is(projectName);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, CIQDashboard.class);
    }

    public List<CIQDashboard> findAll() {
        Criteria criteria = new Criteria();
        criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        return template.find(query, CIQDashboard.class);
    }

    public void deleteByIds(List<String> ids) {
        Criteria criteria = Criteria.where("_id").in(ids);
        if (!userValidationService.isAdmin()) criteria.orOperator(getCriteria());
        Query query = new Query().addCriteria(criteria);
        template.remove(query, CIQDashboard.class);
    }

    private Criteria[] getCriteria() {
        Criteria[] criteriaArray = new Criteria[2];
        criteriaArray[0] = Criteria.where("openAccess").in(false, null).and("createdUser").is(userValidationService.getCurrentUserEmailId());
        criteriaArray[1] = Criteria.where("openAccess").is(true).and("projectName").in(userValidationService.getCurrentUserProjectNames());
        return criteriaArray;
    }
}
