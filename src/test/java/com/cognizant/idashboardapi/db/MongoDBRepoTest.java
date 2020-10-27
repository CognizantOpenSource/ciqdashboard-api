package com.cognizant.idashboardapi.db;

import com.cognizant.idashboardapi.client.NLPApiClient;
import com.cognizant.idashboardapi.models.IDChartItem;
import com.cognizant.idashboardapi.models.ProjectMapping;
import com.cognizant.idashboardapi.repos.ProjectMappingRepository;
import com.cognizant.idashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MongoDBRepoTest {

    @Autowired
    IDChartItemRepositoryImpl repository;
    @Autowired
    NLPApiClient nlpApiClient;
    @Autowired
    MongoTemplate template;
    @Autowired
    ProjectMappingRepository projectMappingRepository;

    @Test
    void findByUserIdOrTeamIds(){
        List<ProjectMapping> test = projectMappingRepository.findByUserIdsContainingOrTeamIdsIn("test", Arrays.asList("5f9010d07c8f8360262e1f42"));
        System.out.println(test);
        Assertions.assertNotNull(projectMappingRepository);
    }

    @Test
    void findTest() {
        Criteria criteria = Criteria.where("name").is("anji");
        MatchOperation match = Aggregation.match(criteria);
//        List<Document> studentDetails = template.find(new Query(criteria), Document.class, "studentDetails");
        Aggregation aggregation = Aggregation.newAggregation(match);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, "studentDetails", Document.class);
        System.out.println(aggregate);
        Assertions.assertNotNull(template);
    }

    @Test
    void queryTest() {
        Criteria criteria = new Criteria();
        criteria.and("projectId").is(51).where("test").is(null);
//        criteria.orOperator("","").andOperator("","")
        Assertions.assertNotNull(criteria);
    }

    @Test
    void nplApiClientTest() {
        List<String> keywords = nlpApiClient.getKeywords("get defects and tests and new");
        System.out.println(keywords);
        Assertions.assertNotNull(nlpApiClient);
    }

    @Test
    void repoTest() {
        List<IDChartItem> test = repository.findByNameLikeIgnoreCase(Arrays.asList("test", "abc", "new", "pie1"));
        System.out.println("test");
        Assertions.assertNotNull(nlpApiClient);
    }
}
