package com.cognizant.idashboardapi.db;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import java.util.Collections;
import java.util.List;

public class CustomAggregationOperation implements AggregationOperation {
    private String jsonOperation;

    public CustomAggregationOperation(String jsonOperation) {
        this.jsonOperation = jsonOperation;
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {
        return context.getMappedObject(Document.parse(jsonOperation));
    }

    @Override
    public List<Document> toPipelineStages(AggregationOperationContext context) {
        return Collections.singletonList(toDocument(context));
    }
}
