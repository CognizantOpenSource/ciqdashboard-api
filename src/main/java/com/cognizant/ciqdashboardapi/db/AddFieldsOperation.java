package com.cognizant.ciqdashboardapi.db;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.DateOperators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddFieldsOperation implements AggregationOperation {

    private final List<Document> documents;

    public AddFieldsOperation(Map<String, String> fieldAndAliasPair) {
        documents = new ArrayList<>();
        fieldAndAliasPair.entrySet().forEach(entry -> {
            documents.add(new Document(entry.getValue(), DateOperators.DateToString.dateOf(entry.getKey())
                    .toString("%Y-%m-%d").toDocument(Aggregation.DEFAULT_CONTEXT)));
        });
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {
        return null;
    }

    @Override
    public List<Document> toPipelineStages(AggregationOperationContext context) {
        return documents.stream().map(document -> new Document("$addFields", document)).collect(Collectors.toList());
    }
}
