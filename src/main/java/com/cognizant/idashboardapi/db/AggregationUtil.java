package com.cognizant.idashboardapi.db;

import com.cognizant.idashboardapi.common.Constants;
import com.cognizant.idashboardapi.models.BarChartAggregation;
import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.models.chart.LeapSort;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 784420 on 10/21/2019 2:49 PM
 */
@Service
public class AggregationUtil {

    String name = Constants.CHART_FIELD_NAME;
    String value = Constants.CHART_FIELD_VALUE;
    String children = Constants.CHART_FIELD_CHILDREN;
    String underscoreId = Constants.UNDERSCORE_ID;
    String dollarIdS = Constants.DOLLAR_ID_S;
    String dollar = Constants.DOLLAR;
    String id = Constants.CHART_FIELD_ID;
    String date = Constants.CHART_FIELD_DATE;
    String series = Constants.CHART_FIELD_SERIES;
    String dateFormatYmd = Constants.CHART_FIELD_DATE_FORMAT_YMD;

    @Autowired
    private MongoTemplate template;
    @Autowired
    private FilterComponent filterComponent;

    /**
     * getting aggregation based on provided data
     *
     * @param filters
     * @param fields
     * @param returnFields
     * @param level
     * @return
     */
    public Aggregation getAggregation(List<FilterConfig> filters, String[] fields, List<String> returnFields, int level, Map<String, String> map) {
        List<AggregationOperation> aggregations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(filters)) {
            aggregations.addAll(filterComponent.getMatchOperation(filters, map));
        }

        aggregations.addAll(addGroupOperation(fields, returnFields, level));

        return Aggregation.newAggregation(aggregations);
    }

    public LocalDate getLocalDate(String date, String formatString) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(formatString));
    }

    public String getEarlierLocalDate() {
        return LocalDate.now().minusMonths(Constants.STATIC_MONTHS).format(DateTimeFormatter.ofPattern(Constants.CHART_FIELD_DATE_FORMAT_STRING));
    }

    public Aggregation getDataGridAggregation(List<FilterConfig> filters, List<String> fields, Map<String, Object> fieldWithAlias,
                                              List<String> excludeFields, LeapSort sort, int limit, Map<String, String> collection) {
        List<AggregationOperation> operations = new ArrayList<>();
        if (!CollectionUtils.isEmpty(filters)) {
            operations.addAll(filterComponent.getMatchOperation(filters, collection));
        }
        if (null != sort) {
            List<String> sortFields = new ArrayList<>(sort.getFields());
            if (sort.getType().equals(LeapSort.SortType.ASC) || sort.getType().equals(LeapSort.SortType.DESC)) {
                operations.add(getSort(sort.getSortDirection(), sortFields.toArray(new String[sortFields.size()])));
            }
        }
        if (limit > 0) {
            operations.add(getLimit(limit));
        }

        operations.add(getProjection(fields, fieldWithAlias, excludeFields));
        return Aggregation.newAggregation(operations);
    }

    public Aggregation getBarChartAggregation(BarChartAggregation aggregation, Map<String, String> collection) {
        List<AggregationOperation> operations = new ArrayList<>();
//        List<Filter> filters = aggregation.getFilterList();
        List<String> groupBy = new ArrayList<>(aggregation.getGroupBy());
        @NotEmpty(message = "Field1 should not null/empty") String field1 = groupBy.get(0);
        @NotEmpty(message = "Field2 should not null/empty") String field2 = groupBy.get(1);
        if (!CollectionUtils.isEmpty(aggregation.getFilters())) {
            operations.addAll(filterComponent.getMatchOperation(aggregation.getFilters(), collection));
        }
        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression(underscoreId).as(name)
                .andExpression(value).as(value)
                .andExpression(series).as(series);
        operations.add(Aggregation.group(field1, field2).count().as(value));
        operations.add(Aggregation.group(field1).sum(field2).as(value)
                .push(
                        new BasicDBObject(name, String.format(dollarIdS, field2))
                                .append(value, String.format(dollar, value))
                ).as(series)
        );
        operations.add(projectionOperation);
        return Aggregation.newAggregation(operations);
    }

    private AggregationOperation getLimit(int limit) {
        return Aggregation.limit(limit);
    }

    /**
     * Get sort aggregation operation based on direction and fields
     *
     * @param direction {@link Sort.Direction} ascending or descending
     * @param fields    field details
     * @return {@link AggregationOperation}
     */
    private AggregationOperation getSort(Sort.Direction direction, String... fields) {
        return Aggregation.sort(direction, fields);
    }

    /**
     * preparing projection operation based on input
     *
     * @param fields          field names with out alias name
     * @param fieldsWithAlias fields with alias name, here key is alias name and value is field name. for concat we can pass like "concat(fieldName1,'concatStringWithSingleQuotes', fieldName2)"
     * @param excludeFields   field names to exclude
     * @return ProjectionOperation
     */
    private AggregationOperation getProjection(@NonNull List<String> fields, @NonNull Map<String, Object> fieldsWithAlias, @NonNull List<String> excludeFields) {
        ProjectionOperation project;
        if (!CollectionUtils.isEmpty(fields)) {
            project = Aggregation.project(fields.toArray(new String[fields.size()]));
        } else {
            project = Aggregation.project();
        }

        if (!CollectionUtils.isEmpty(fieldsWithAlias)) {
            Set<Map.Entry<String, Object>> entries = fieldsWithAlias.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object fieldName = entry.getValue();
                if (fieldName instanceof String && ((String) fieldName).contains("concat("))
                    project = project.andExpression(((String) fieldName)).as(entry.getKey());
                else if (fieldName instanceof String)
                    project = project.and(((String) fieldName)).as(entry.getKey());
                else
                    project = project.and((AggregationExpression) fieldName).as(entry.getKey());
            }
        }
        if (!CollectionUtils.isEmpty(excludeFields)) {
            project = project.andExclude(excludeFields.toArray(new String[excludeFields.size()]));
        }

        return project;
    }

    /**
     * Preparing group aggregation with projection
     *
     * @param fields       list of fields for the grouping
     * @param returnFields return field names
     * @param level        grouping level (as of now 3 levels)
     * @return Group aggregation
     */
    private List<AggregationOperation> addGroupOperation(@NonNull String[] fields, @NonNull List<String> returnFields, int level) {
        List<AggregationOperation> aggregations = new ArrayList<>();

        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression(underscoreId).as(name)
                .andExpression(value).as(value);

        switch (level) {
            case 1:
                aggregations.add(Aggregation.group(fields[0]).count().as(returnFields.get(1)));
                aggregations.add(projectionOperation);
                break;
            case 2:
                aggregations.add(Aggregation.group(fields[0], fields[1]).count().as(returnFields.get(1)));
                aggregations.add(Aggregation.group(fields[0]).sum(returnFields.get(1)).as(returnFields.get(1))
                        .push(
                                new BasicDBObject(returnFields.get(0), String.format(dollarIdS, fields[1]))
                                        .append(returnFields.get(1), String.format(dollar, returnFields.get(1)))
                        ).as(returnFields.get(2))
                );
                aggregations.add(projectionOperation.andExpression(children).as(children));
                break;
            case 3:
            default:
                aggregations.add(Aggregation.group(fields[0], fields[1], fields[2]).count().as(returnFields.get(1)));
                aggregations.add(Aggregation.group(fields[0], fields[1]).sum(returnFields.get(1)).as(returnFields.get(1))
                        .push(
                                new BasicDBObject(returnFields.get(0), String.format(dollarIdS, fields[2]))
                                        .append(returnFields.get(1), String.format(dollar, returnFields.get(1)))
                        ).as(returnFields.get(2))
                );
                aggregations.add(Aggregation.group(fields[0]).sum(returnFields.get(1)).as(returnFields.get(1))
                        .push(
                                new BasicDBObject(returnFields.get(0), String.format(dollarIdS, fields[1]))
                                        .append(returnFields.get(1), String.format(dollar, returnFields.get(1)))
                                        .append(returnFields.get(2), String.format(dollar, returnFields.get(2)))
                        ).as(returnFields.get(2)));
                aggregations.add(projectionOperation.andExpression(children).as(children));
                break;
        }
        return aggregations;
    }

    public List<AggregationOperation> getMatchOperation(List<FilterConfig> filters, Map<String, String> fieldsAndTypes){
        return filterComponent.getMatchOperation(filters, fieldsAndTypes);
    }
}
