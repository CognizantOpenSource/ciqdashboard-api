package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.models.*;
import com.cognizant.idashboardapi.models.aggregate.FieldAggregate;
import com.cognizant.idashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.idashboardapi.models.chart.DBSort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cognizant.idashboardapi.common.Constants.*;

@Service
public class ValidatorService {

    public DrillDownChartAggregation validateChartAggregation(DrillDownChartAggregation chartAggregation, Set<String> fields) {
        List<String> groupBy = chartAggregation.getGroupBy();
        List<Filter> filters = chartAggregation.getFilterList();
        if (CollectionUtils.isEmpty(groupBy)) {
            throw new InvalidDetailsException("GroupBy should have at least one field");
        }
        if (chartAggregation.getLevel() == 0 && !CollectionUtils.isEmpty(groupBy)) {
            chartAggregation.setLevel(Math.min(groupBy.size(), 3));
        }
        if (!(chartAggregation.getLevel() > 0 && chartAggregation.getLevel() < 4)) {
            throw new InvalidDetailsException("Level should be more than 0 and less than 4");
        }
        validateFields(fields, groupBy, GROUP_BY);
        validateFilters(filters, fields);

        List<String> distinct = chartAggregation.getGroupBy().stream().distinct().collect(Collectors.toList());
        chartAggregation.setGroupBy(distinct);

        return chartAggregation;
    }

    public DrillDownChartAggregation validateLineChartAggregation(DrillDownChartAggregation chartAggregation, Set<String> fields) {
        chartAggregation.setLevel(1);
        List<String> groupBy = chartAggregation.getGroupBy();
        List<Filter> filters = chartAggregation.getFilterList();
        validateFields(fields, groupBy, GROUP_BY);
        validateFilters(filters, fields);
        return chartAggregation;
    }

    public BarChartAggregation validateBarChartAggregation(BarChartAggregation barChartAggregation, Set<String> fields) {
        List<String> groupBy = barChartAggregation.getGroupBy();
        List<Filter> filters = barChartAggregation.getFilterList();
        validateFields(fields, groupBy, GROUP_BY);
        validateFilters(filters, fields);
        List<String> distinct = groupBy.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupBy) || distinct.size() < 2) {
            throw new InvalidDetailsException("GroupBy fields list size should be 2 ");
        }
        if (distinct.size() > 2) distinct = distinct.subList(0, 2);
        barChartAggregation.setGroupBy(distinct);
        return barChartAggregation;
    }

    public BarChartAggregation validateBarGaugeChartAggregation(BarChartAggregation barChartAggregation, Set<String> fields) {
        List<Filter> filters = barChartAggregation.getFilterList();
        validateFilters(filters, fields);
        return barChartAggregation;
    }

    public TableAggregation validateTableAggregation(TableAggregation tableAggregation, Set<String> fields) {
        List<Filter> filters = tableAggregation.getFilterList();

        List<String> projection = tableAggregation.getProjection();
        LinkedHashMap<String, Object> projectionWithAlias = tableAggregation.getProjectionWithAlias();
        if (CollectionUtils.isEmpty(projection) && CollectionUtils.isEmpty(projectionWithAlias)) {
            throw new InvalidDetailsException("Projection should have at least one field");
        }
        List<String> projectionAlias = projectionWithAlias.values().stream()
                .filter(fieldName -> fieldName instanceof String && !((String) fieldName).contains("concat("))
                .map(Object::toString)
                .collect(Collectors.toList());
        List<String> excludeFields = tableAggregation.getExcludeFields();
        DBSort sort = tableAggregation.getSort();

        validateFilters(filters, fields);
        validateFields(fields, projection, "Projection");
        validateFields(fields, projectionAlias, "ProjectionWithAlias");
        validateFields(fields, excludeFields, "ExcludeFields");
        if (null != sort) validateFields(fields, sort.getFields(), "SortFields");


        return tableAggregation;
    }

    public AggregateConfig validateAggregate(AggregateConfig aggregate, Set<String> fields, Map<String, String> fieldsAndTypes) {
        if (CollectionUtils.isEmpty(aggregate.getFields()))
            throw new InvalidDetailsException("Aggregate fields should have at least one field");
        List<String> distinct = aggregate.getFields().stream().distinct().collect(Collectors.toList());
        if (distinct.size() > 2) distinct = distinct.subList(0, 2);
        validateFields(fields, distinct, AGGREGATE_FIELDS);
        if (!Type.AggregateType.COUNT.equals(aggregate.getType())) {
            for (String field : distinct) {
                String type = fieldsAndTypes.get(field);
                if (!NUMBER_FIELD_TYPES.contains(type)) {
                    throw new InvalidDetailsException("Aggregate fields should be number type");
                }
            }
        }
        aggregate.setFields(distinct);
        return aggregate;
    }

    public GroupAggregate validateAggregate(GroupAggregate aggregate, Set<String> fields, Map<String, String> fieldsAndTypes) {
        aggregate.getGroups().forEach(group -> {
            if (null == group.getOperator())
                throw new InvalidDetailsException("Operator should not be null");
            List<String> distinct = group.getAggregates().stream()
                    .filter(fieldAggregate -> fieldAggregate.getType() != Type.AggregateType.CONSTANT)
                    .map(FieldAggregate::getField).distinct().collect(Collectors.toList());
            validateFields(fields, distinct, AGGREGATE_FIELDS);
            group.getAggregates().forEach(fieldAggregate -> fieldAggregateValidation(fieldAggregate, fieldsAndTypes));
        });
        return aggregate;
    }

    private void fieldAggregateValidation(FieldAggregate fieldAggregate, Map<String, String> fieldsAndTypes) {
        if (null == fieldAggregate.getOperator())
            throw new InvalidDetailsException("Operator should not be null");
        if (Type.AggregateType.CONSTANT.equals(fieldAggregate.getType())) {
            Double value = fieldAggregate.getValue();
            if (null == value || value <= 0)
                throw new InvalidDetailsException("Value should be greater than zero");
        } else if (!(Type.AggregateType.COUNT.equals(fieldAggregate.getType()) || Type.AggregateType.DISTINCT_COUNT.equals(fieldAggregate.getType()))) {
            String type = fieldsAndTypes.get(fieldAggregate.getField());
            if (!NUMBER_FIELD_TYPES.contains(type)) {
                throw new InvalidDetailsException("Aggregate fields should be number type");
            }
        }
    }

    private void validateFilters(List<Filter> filters, Set<String> fields) {
        if (!CollectionUtils.isEmpty(filters)) {
            List<String> keys = filters.stream().map(Filter::getField).collect(Collectors.toList());
            validateFields(fields, keys, FILTERS);
        }
    }

    private void validateFields(Set<String> fields, List<String> keys, String type) {
        if (!fields.containsAll(keys)) {
            throw new InvalidDetailsException(type + " fields are invalid, it should be in the DB Collection");
        }
    }
}
