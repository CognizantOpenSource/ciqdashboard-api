package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.models.*;
import com.cognizant.idashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.idashboardapi.models.chart.data.ChartData;
import com.cognizant.idashboardapi.models.chart.data.DataGridRow;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class ChartAggregationComponent {

    @Autowired
    IDashboardDataSourceService dataSourceService;
    @Autowired
    CollectorService collectorService;
    @Autowired
    ValidatorService validatorService;

    public IDChartItemDataDTO getChartAggregation(IDChartItem chartItem) {
        Optional<Type.ChartItemType> type = Type.ChartItemType.getChartItemType(chartItem.getType());
        if (type.isPresent() && type.get() == Type.ChartItemType.COMBO) {
            List<ChartData> data = new ArrayList<>();
            IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
            Map<String, ComboChartGroup> comboGroupBy = chartItem.getComboGroupBy();
            if (CollectionUtils.isEmpty(comboGroupBy))
                throw new InvalidDetailsException("ComboGroupBy should be empty/null if type is 'combo'");
            comboGroupBy.entrySet().forEach(entry -> {
                IDChartItem item = new IDChartItem();
                BeanUtils.copyProperties(chartItem, item);
                ComboChartGroup combo = entry.getValue();
                item.setGroupBy(null);
                if (!CollectionUtils.isEmpty(combo.getGroupBy())) item.setGroupBy(combo.getGroupBy());
                if (!CollectionUtils.isEmpty(combo.getFilters())) item.getFilters().addAll(combo.getFilters());
                item.setType(combo.getType());
                IDChartItemDataDTO result = getChartAggregationResult(item);
                data.add(new ChartData(entry.getKey(), null, (List<ChartData>) result.getData(), null));
                item = null;
            });
            dataDTO.setChartItemDetails(chartItem);
            dataDTO.setData(data);
            return dataDTO;
        } else {
            return getChartAggregationResult(chartItem);
        }
    }

    public IDChartItemDataDTO getChartAggregationResult(IDChartItem chartItem) {
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType(chartItem.getType());
        Type.ChartItemType chartItemType = optional.isPresent() ? optional.get() : Type.ChartItemType.PIE_CHART;
        Type.GenericChartItemType genericChartItemType = Type.getGenericChartItemType(chartItemType);
        if (CollectionUtils.isEmpty(chartItem.getGroupBy()) && null != chartItem.getAggregate()) {
            genericChartItemType = Type.GenericChartItemType.AGGREGATE;
        }

        IDashboardDataSource source = dataSourceService.assertAndGetByName(chartItem.getSource());
        Set<String> fields = collectorService.getFieldsByCollection(source.getCollectionName());

        if (CollectionUtils.isEmpty(chartItem.getFilters())) chartItem.setFilters(new ArrayList<>());
        if (CollectionUtils.isEmpty(chartItem.getGroupBy())) chartItem.setGroupBy(new ArrayList<>());

        Object chartData;
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();

        switch (genericChartItemType) {
            case DRILL_DOWN_CHART:
                chartData = getDrillDownChart(chartItem, fields, source.getCollectionName());
                break;
            case BAR_CHART:
                chartData = getBarChart(chartItem, fields, source.getCollectionName());
                break;
            case BAR_GAUGE_CHART:
                chartData = getBarGaugeChart(chartItem, fields, source.getCollectionName());
                break;
            case TABLE:
                chartData = getTable(chartItem, fields, source.getCollectionName());
                break;
            case AGGREGATE:
                chartData = getFieldsAggregate(chartItem, fields, source.getCollectionName());
                break;
            case NONE:
                chartData = null;
                break;
            case LINE:
            default:
                chartData = getLineChart(chartItem, fields, source.getCollectionName());
                break;
        }
        dataDTO.setChartItemDetails(chartItem);
        dataDTO.setData(chartData);
        return dataDTO;
    }

    private List<ChartData> getFieldsAggregate(IDChartItem chartItem, Set<String> fields, String collectionName) {
        List<FilterConfig> filters = chartItem.getFilters();
        GroupAggregate aggregate = chartItem.getAggregate();
        String title = (String) chartItem.getOptions().get("title");
        title = StringUtils.isEmpty(title) ? "" : title;
        aggregate.setName(title);
        Map<String, String> fieldsAndTypes = collectorService.getFieldsAndTypesByCollection(collectionName);
        GroupAggregate validateAggregate = validatorService.validateAggregate(aggregate, fields, fieldsAndTypes);
        return collectorService.getGroupAggregate(filters, validateAggregate, fields, collectionName);
    }

    private List<ChartData> getLineChart(IDChartItem chartItem, Set<String> fields, String collectionName) {
        LineChartAggregation lineChartAggregation = new LineChartAggregation();
        if (CollectionUtils.isEmpty(chartItem.getGroupBy()))
            throw new InvalidDetailsException("GroupBy list should not be empty or null");
        lineChartAggregation.setGroupBy(chartItem.getGroupBy().get(0));
        lineChartAggregation.setFilters(chartItem.getFilters());
        DrillDownChartAggregation validateLineChartAggregation = validatorService.validateLineChartAggregation(lineChartAggregation.getChartAggregation(), fields);
        return collectorService.getChart(validateLineChartAggregation, collectionName);
    }

    private List<DataGridRow> getTable(IDChartItem chartItem, Set<String> fields, String collectionName) {
        TableAggregation tableAggregation = new TableAggregation();
        tableAggregation.setFilters(chartItem.getFilters());
        tableAggregation.setProjection(chartItem.getProjection());
        Object maxRecords = chartItem.getOptions().get("maxRecords");
        if (Objects.nonNull(maxRecords) && maxRecords instanceof Integer) {
            tableAggregation.setLimit((Integer) maxRecords);
        }
        TableAggregation validateTableAggregation = validatorService.validateTableAggregation(tableAggregation, fields);
        return collectorService.getTable(validateTableAggregation, collectionName);
    }

    private List<ChartData> getBarChart(IDChartItem chartItem, Set<String> fields, String collectionName) {
        BarChartAggregation barChartAggregation = new BarChartAggregation();
        barChartAggregation.setFilters(chartItem.getFilters());
        barChartAggregation.setGroupBy(chartItem.getGroupBy());
        BarChartAggregation validateBarChartAggregation = validatorService.validateBarChartAggregation(barChartAggregation, fields);
        return collectorService.getBarChart(validateBarChartAggregation, collectionName);
    }

    private List<LinkedHashMap> getBarGaugeChart(IDChartItem chartItem, Set<String> fields, String collectionName) {
        BarChartAggregation barChartAggregation = new BarChartAggregation();
        barChartAggregation.setFilters(chartItem.getFilters());
        BarChartAggregation validateBarGaugeChartAggregation = validatorService.validateBarGaugeChartAggregation(barChartAggregation, fields);
        return collectorService.getBarGaugeChart(validateBarGaugeChartAggregation, collectionName);
    }

    private List<ChartData> getDrillDownChart(IDChartItem chartItem, Set<String> fields, String collectionName) {
        DrillDownChartAggregation drillDownChartAggregation = new DrillDownChartAggregation();
        drillDownChartAggregation.setFilters(chartItem.getFilters());
        drillDownChartAggregation.setGroupBy(chartItem.getGroupBy());
        DrillDownChartAggregation validateChartAggregation = validatorService.validateChartAggregation(drillDownChartAggregation, fields);
        return collectorService.getChart(validateChartAggregation, collectionName);
    }

}
