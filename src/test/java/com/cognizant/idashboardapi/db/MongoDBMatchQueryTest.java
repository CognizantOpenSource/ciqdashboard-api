package com.cognizant.idashboardapi.db;

import com.cognizant.idashboardapi.models.Filter;
import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.repos.impl.CollectorRepositoryImpl;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;

import static com.cognizant.idashboardapi.models.Filter.OPType.*;

@SpringBootTest
class MongoDBMatchQueryTest {

    @Autowired
    MongoTemplate template;
    @Autowired
    FilterComponent filterComponent;
    @Autowired
    CollectorRepositoryImpl collectorRepository;

    private static final String GITLAB_COMMITS = "source_gitlab_commits";


    @Test
    void testMatches(){
        Set<String> collectionNames = template.getCollectionNames();
        Assertions.assertNotNull(collectionNames);
    }

    @Test
    void testDateGt(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("committedDate", gte, "2010-11-29T07:24:36.000Z"));
        runAggregate(filters);
    }

    @Test
    void testInNin(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("projectName", in, Arrays.asList("execution-ui", "execution-robot")));
        filters.add(new Filter("projectId", nin, Arrays.asList(33, 39)));
        runAggregate(filters);
    }

    @Test
    void testStartsWithEndsWithContains(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("projectName", startswith, "execution"));
        filters.add(new Filter("authorEmail", endswith, "@cognizant.com"));
        filters.add(new Filter("branchName", contains, "ste"));
        runAggregate(filters);
    }

    @Test
    void testGteLte(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("projectId", gte, 20));
        filters.add(new Filter("projectId", lte, 20));
        runAggregate(filters);
    }

    @Test
    void testGtLt(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("projectId", gt, 19));
        filters.add(new Filter("projectId", lt, 21));
        runAggregate(filters);
    }

    @Test
    void testEquals(){
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("projectName", eq, "execution-robot"));
        filters.add(new Filter("projectId", equals, 20));
        filters.add(new Filter("projectId", ne, 21));
        runAggregate(filters);
    }

    @Test
    void dateFilter(){
        AddFieldsOperation addFieldsOperation = new AddFieldsOperation(Collections.singletonMap("sprintStartDate", "customStringDate"));
        MatchOperation criteria = Aggregation.match(Criteria.where("customStringDate").is("2020-07-08"));
        Aggregation aggregation = Aggregation.newAggregation(addFieldsOperation, criteria);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, "jiraIssues", Document.class);

    }

    @Test
    void ifNullTest(){
        Criteria in = Criteria.where("projectKey").is("ID").and("issueTypeId").in(Arrays.asList("10004", "10002"));
        MatchOperation match = Aggregation.match(in);
        ProjectionOperation project = Aggregation.project();
                project = project.and(ConditionalOperators.ifNull("sprintStartDate").then("--NA--")).as("sprintStartDate");
        Aggregation aggregation = Aggregation.newAggregation(match, project);
        AggregationResults<Document> aggregate = template.aggregate(aggregation, "jiraIssues", Document.class);

    }

    private void runAggregate(List<Filter> filters){
        Map collection = collectorRepository.getFieldsAndTypesByCollection(GITLAB_COMMITS);
        FilterConfig filterConfig = new FilterConfig();
        filterConfig.setConfigs(filters);
        filterConfig.setName("Test");
        Criteria criteria = filterComponent.getCriteria(Arrays.asList(filterConfig), collection, new HashMap<>());
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.limit(10));
        AggregationResults<Document> aggregate = template.aggregate(aggregation, "gitlab_commits", Document.class);
        List<Document> mappedResults = aggregate.getMappedResults();
        Assertions.assertNotNull(mappedResults);
    }
}
