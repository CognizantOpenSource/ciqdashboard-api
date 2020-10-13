package com.cognizant.idashboardapi.db;

import com.cognizant.idashboardapi.client.NLPApiClient;
import com.cognizant.idashboardapi.models.IDChartItem;
import com.cognizant.idashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    @Test
    void queryTest(){
        Criteria criteria = new Criteria();
        criteria.and("projectId").is(51).where("test").is(null);
//        criteria.orOperator("","").andOperator("","")
    }

    @Test
    void nplApiClientTest(){
        List<String> keywords = nlpApiClient.getKeywords("get defects and tests and new");
        System.out.println(keywords);
        Assertions.assertNotNull(nlpApiClient);
    }

    @Test
    void repoTest(){
        List<IDChartItem> test = repository.findByNameLikeIgnoreCase(Arrays.asList("test", "abc", "new", "pie1"));
        System.out.println("test");
        Assertions.assertNotNull(nlpApiClient);
    }
}
