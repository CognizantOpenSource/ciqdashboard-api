package com.cognizant.idashboardapi.db;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.models.Type;
import com.cognizant.idashboardapi.models.aggregate.FieldAggregate;
import com.cognizant.idashboardapi.models.aggregate.FieldAggregateGroup;
import com.cognizant.idashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.idashboardapi.models.chart.data.ChartData;
import com.cognizant.idashboardapi.repos.impl.CollectorRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cognizant.idashboardapi.common.Constants.COLLECTION_S_NOT_AVAILABLE_IN_DB;

@Component
public class GroupAggregateComponent {
    @Autowired
    AggregationUtil aggregationUtil;
    @Autowired
    DBAggregationUtilComponent dbAggregationUtilComponent;
    @Autowired
    CollectorRepositoryImpl collectorRepository;

    public List<ChartData> getGroupAggregate(List<FilterConfig> filters, GroupAggregate groupAggregate, Set<String> fields, String collectionName) {
        List<Double> groupResults = new ArrayList<>();
        String name = StringUtils.isEmpty(groupAggregate.getName()) ? "result" : groupAggregate.getName();
        ChartData chartData = new ChartData(name, 0D, null, null);
        groupAggregate.getGroups().forEach(group -> {
            List<Double> fieldResults = new ArrayList<>();
            group.getAggregates().forEach(fieldAggregate -> {
                List<FilterConfig> fieldFilters = new ArrayList<>();
                if (!CollectionUtils.isEmpty(filters)) fieldFilters.addAll(filters);
                if (!CollectionUtils.isEmpty(fieldAggregate.getFilters()))
                    fieldFilters.addAll(fieldAggregate.getFilters());

                fieldResults.add(getFieldAggregate(fieldFilters, fieldAggregate, fields, collectionName));
            });

            Double groupValue = getAggregate(fieldResults, group.getAggregates().stream().map(FieldAggregate::getOperator).collect(Collectors.toList()));
            groupResults.add(groupValue);
        });
        chartData.setValue(getAggregate(groupResults, groupAggregate.getGroups().stream().map(FieldAggregateGroup::getOperator).collect(Collectors.toList())));
        return Arrays.asList(chartData);
    }

    public Double getFieldAggregate(List<FilterConfig> filters, FieldAggregate validateAggregate, Set<String> fields, String collectionName) {
        Type.AggregateType type = validateAggregate.getType();
        if (type.equals(Type.AggregateType.CONSTANT)) return validateAggregate.getValue();
        if (!collectorRepository.getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format(COLLECTION_S_NOT_AVAILABLE_IN_DB, collectionName));
//        List<Filter> filterList = filters.stream().flatMap(filterConfig -> filterConfig.getConfigs().stream()).collect(Collectors.toList());
        String field = validateAggregate.getField();
        List<AggregationOperation> matches = aggregationUtil.getMatchOperation(filters, collectorRepository.getFieldsAndTypesByCollection(collectionName));
        return dbAggregationUtilComponent.findFieldAggregate(matches, collectionName, field, type);
    }

    private Double getAggregate(List<Double> results, List<Type.MathOperator> operators) {
        Double resultValue = 0D;
        for (int i = 0; i < results.size(); i++) {
            Double fieldValue = results.get(i);
            if (resultValue == 0) {
                resultValue = fieldValue;
            } else if (fieldValue > 0) {
                Type.MathOperator operator = operators.get(i);
                switch (operator) {
                    case div:
                        resultValue = (resultValue / fieldValue);
                        break;
                    case mul:
                        resultValue = (resultValue * fieldValue);
                        break;
                    case sub:
                        resultValue = (resultValue - fieldValue);
                        break;
                    case add:
                    default:
                        resultValue = (resultValue + fieldValue);
                        break;
                }
            }
        }
        return resultValue;
    }
}
