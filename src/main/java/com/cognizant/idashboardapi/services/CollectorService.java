package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.db.DBAggregationUtilComponent;
import com.cognizant.idashboardapi.db.GroupAggregateComponent;
import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.models.*;
import com.cognizant.idashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.idashboardapi.models.chart.data.ChartData;
import com.cognizant.idashboardapi.models.chart.data.DataGridRow;
import com.cognizant.idashboardapi.models.db.view.BaseCollectionDetailsForView;
import com.cognizant.idashboardapi.models.db.view.FieldWithAlias;
import com.cognizant.idashboardapi.models.db.view.LocalForeignField;
import com.cognizant.idashboardapi.models.db.view.ViewDetails;
import com.cognizant.idashboardapi.repos.impl.CollectorRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectorService {
    @Autowired
    CollectorRepositoryImpl collectorRepository;
    @Autowired
    DBAggregationUtilComponent dbAggregationUtilComponent;
    @Autowired
    ValidatorService validatorService;
    @Autowired
    GroupAggregateComponent groupAggregateComponent;
    @Autowired
    IDashboardDataSourceService dataSourceService;


    /**
     * Getting all collection names from databasd
     *
     * @return set of collections
     */
    public Set<String> getCollectionNames() {
        return collectorRepository.getCollectionNames();
    }

    /**
     * Getting all fields based on {collectionName}
     *
     * @param collectionName name of collection
     * @return set of field names
     */
    public Set<String> getFieldsByCollection(String collectionName) {
        return collectorRepository.getFieldsByCollection(collectionName);
    }

    /**
     * Getting distinct values based on collection and field
     *
     * @param collectionName name of collection
     * @param fieldName      name of field name
     * @return set of field values
     */
    public Set<String> getDistinctValuesByField(String collectionName, String fieldName) {
        return collectorRepository.getDistinctValuesByFieldName(collectionName, fieldName);
    }

    public Set<String> getDistinctValuesBySourceField(String sourceName, String fieldName) {
        IDashboardDataSource iDashboardDataSource = dataSourceService.assertAndGetByName(sourceName);
        return getDistinctValuesByField(iDashboardDataSource.getCollectionName(), fieldName);
    }

    /**
     * Getting Chart based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link ChartData}
     */
    public List<ChartData> getChart(DrillDownChartAggregation chartAggregation, String collectionName) {
        return collectorRepository.getChart(chartAggregation, collectionName);
    }

    /**
     * Getting BarChart based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link ChartData}
     */
    public List<ChartData> getBarChart(BarChartAggregation chartAggregation, String collectionName) {
        Map<String, String> collection = this.getFieldsAndTypesByCollection(collectionName);
        return collectorRepository.getBarChart(chartAggregation, collectionName, collection);
    }

    /**
     * Getting Table based on provided chart aggregation
     *
     * @param chartAggregation aggregation details for the chart
     * @param collectionName   name of collection
     * @return {@link List} of {@link DataGridRow}
     */
    public List<DataGridRow> getTable(TableAggregation chartAggregation, String collectionName) {
        return collectorRepository.getTable(chartAggregation, collectionName);
    }

    /**
     * Getting fields and types based on collection
     *
     * @param collectionName name of collection
     * @return Map, field and type as pair (String, String)
     */
    public List<FieldType> getFieldsAndTypes(String collectionName) {
        return collectorRepository.getFieldsAndTypes(collectionName);
    }

    public Map<String, String> getFieldsAndTypesByCollection(String collectionName) {
        return collectorRepository.getFieldsAndTypesByCollection(collectionName);
    }

    public List<ChartData> getFieldAggregate(List<FilterConfig> filters, AggregateConfig validateAggregate, Set<String> fields, String collectionName) {
        return collectorRepository.getFieldAggregate(filters, validateAggregate, fields, collectionName);
    }

    public List<ChartData> getGroupAggregate(List<FilterConfig> filters, GroupAggregate groupAggregate, Set<String> fields, String collectionName) {
        return groupAggregateComponent.getGroupAggregate(filters, groupAggregate, fields, collectionName);
    }

    public boolean createView(ViewDetails viewDetails) {
        viewValidation(viewDetails);
        return dbAggregationUtilComponent.createView(viewDetails);
    }

    public void deleteView(String collectionName) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format("Collection(%s) not available in DB", collectionName));
        dbAggregationUtilComponent.deleteView(collectionName);
    }

    public void viewValidation(ViewDetails viewDetails) {
        if (getCollectionNames().contains(viewDetails.getName()))
            throw new InvalidDetailsException(String.format("View already exist with mentioned name(%s) ", viewDetails.getName()));
        BaseCollectionDetailsForView baseCollection = viewDetails.getBaseCollection();
        List<FieldWithAlias> baseCollectionFields = baseCollection.getFields();
        Set<String> baseFieldNames = baseCollectionFields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
        List<String> aliases = baseCollectionFields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toList());

        viewDetails.getLookups().forEach(lookup -> {
            List<FieldWithAlias> lookupFields = lookup.getFields();
            Set<String> fieldNames = lookupFields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
            aliases.addAll(lookupFields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toList()));
            baseFieldNames.addAll(lookup.getLocalForeignFields().stream().map(LocalForeignField::getLocalField).collect(Collectors.toList()));
            fieldNames.addAll(lookup.getLocalForeignFields().stream().map(LocalForeignField::getForeignField).collect(Collectors.toList()));
            validateCollectionField(lookup.getName(), fieldNames);
            validateFieldAndAliases(lookup.getName(), lookupFields);
        });
        validateCollectionField(baseCollection.getName(), baseFieldNames);
        validateFieldAndAliases(baseCollection.getName(), baseCollectionFields);
        Set<String> duplicateAlias = aliases.stream().filter(alias -> Collections.frequency(aliases, alias) > 1)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(duplicateAlias)) {
            throw new InvalidDetailsException(String.format("found same alias name in multiple collections, duplicateAliasList(%s)", duplicateAlias));
        }
    }

    public void validateFieldAndAliases(@NotEmpty(message = "CollectionName should not be null/empty") String collectionName,
                                        @NotEmpty(message = "Fields should not be null/empty") List<FieldWithAlias> fields) {
        Set<String> fieldNames = fields.stream().map(FieldWithAlias::getName).collect(Collectors.toSet());
        Set<String> aliases = fields.stream().map(FieldWithAlias::getAlias).collect(Collectors.toSet());
        if (fields.size() != fieldNames.size())
            throw new InvalidDetailsException(String.format("found duplicate field name in Collection(%s)", collectionName));
        if (fields.size() != aliases.size())
            throw new InvalidDetailsException(String.format("found duplicate alias name in Collection(%s)", collectionName));
    }

    public void validateCollectionField(@NotEmpty(message = "CollectionName should not be null/empty") String collectionName,
                                        @NotEmpty(message = "Fields should not be null/empty") Set<String> fieldNames) {
        Set<String> fieldsByCollection = getFieldsByCollection(collectionName);
        boolean containsAll = fieldsByCollection.containsAll(fieldNames);
        if (!containsAll)
            throw new InvalidDetailsException(String.format("fields are invalid, it should be in the DB Collection(%s)", collectionName));
    }

    public void addCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        if (getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException("Collection Name " + collectionName + " is already exists in the Database : ");

        collectorRepository.addCustomCollection(collectionName, collectorData);
    }

    public void updateCustomCollection(String collectionName, List<Map<String, Object>> collectorData) {
        if (!getCollectionNames().contains(collectionName))
            throw new InvalidDetailsException(String.format("Collection(%s) not available in DB", collectionName));
        collectorRepository.updateCustomCollection(collectionName, collectorData);
    }
}
