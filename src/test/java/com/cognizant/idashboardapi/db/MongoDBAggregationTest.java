package com.cognizant.idashboardapi.db;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.DEFAULT_CONTEXT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@SpringBootTest
class MongoDBAggregationTest {
    @Autowired
    MongoTemplate template;
    @Autowired
    DBAggregationUtilComponent dbAggregationUtilComponent;

    @Test
    void aggregateTest() {
        Double distinctCount = dbAggregationUtilComponent.getDistinctCount(Arrays.asList(Aggregation.match(Criteria.where("projectId").is(51))),
                "gitlab_commits",
                "projectId");

    }

    @Test
    void lookupTest() {
        List<AggregationOperation> aggregations = new ArrayList<>();
        Document document = Aggregation.match(Criteria.where("sid").is("stuId")).toDocument(DEFAULT_CONTEXT);

        List<BasicDBObject> dbObjects = Arrays.asList(new BasicDBObject("$eq", Arrays.asList("$sid", "$$stuId")));
        BasicDBObject and = new BasicDBObject().append("$and", dbObjects);
        BasicDBObject expr = new BasicDBObject("$expr", and);
        BasicDBObject lookupPipeline = new BasicDBObject().append("from", "parent")
                .append("let", new BasicDBObject("stuId", "$_id"))
                .append("pipeline", Arrays.asList(new BasicDBObject("$match", expr)))
                .append("as", "parents");
        BasicDBObject lookup = new BasicDBObject("$lookup", lookupPipeline);
        CustomAggregationOperation lookupOperation = new CustomAggregationOperation(lookup.toJson());

        aggregations.add(lookupOperation);
        Aggregation aggregation = newAggregation(aggregations);
        List<Document> pipeline = aggregation.toPipeline(DEFAULT_CONTEXT);

        AggregationResults<Document> aggregate = template.aggregate(aggregation, "student", Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        Assertions.assertNotNull(template);

    }
}
