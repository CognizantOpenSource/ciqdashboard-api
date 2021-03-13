package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.models.chart.DBSort;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 784420 on 10/18/2019 2:34 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TableAggregation extends FilterAggregation {
    private List<String> projection = new ArrayList<>();
    private LinkedHashMap<String, Object> projectionWithAlias = new LinkedHashMap<>();
    private List<String> excludeFields = new ArrayList<>();
    private DBSort sort;
    private int limit;

}