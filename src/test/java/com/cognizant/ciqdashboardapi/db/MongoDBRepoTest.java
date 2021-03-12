package com.cognizant.ciqdashboardapi.db;

import com.cognizant.ciqdashboardapi.client.NLPApiClient;
import com.cognizant.ciqdashboardapi.repos.ProjectMappingRepository;
import com.cognizant.ciqdashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

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

    //@Test
    //void findByUserIdOrTeamIds(){
        //List<ProjectMapping> test = projectMappingRepository.findByUserIdsContainingOrTeamIdsIn("test", Arrays.asList("5f9010d07c8f8360262e1f42"));
      //Assertions.assertNotNull(projectMappingRepository);
    //}

    @Test
    void findTest() {
        Criteria criteria = Criteria.where("name").is("anji");
        MatchOperation match = Aggregation.match(criteria);
//        List<Document> studentDetails = template.find(new Query(criteria), Document.class, "studentDetails");
        Aggregation aggregation = Aggregation.newAggregation(match);
       //AggregationResults<Document> aggregate = template.aggregate(aggregation, "studentDetails", Document.class);
        Assertions.assertNotNull(template);
    }

    //@Test
    //void nplApiClientTest() {
        //List<String> keywords = nlpApiClient.getKeywords("get defects and tests and new");
        //Assertions.assertNotNull(nlpApiClient);
    //}

    //@Test
    //void repoTest() {
        //List<IDChartItem> test = repository.findByNameLikeIgnoreCase(Arrays.asList("test", "abc", "new", "pie1"));
        //Assertions.assertNotNull(nlpApiClient);
    //}
}
