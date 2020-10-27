package com.cognizant.idashboardapi.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

@SpringBootTest
public class MongoDBUpdateTest {
    @Autowired
    MongoTemplate template;

    @Test
    void simpleTest(){
        Set<String> collectionNames = template.getCollectionNames();
        System.out.println(collectionNames);
    }
}
