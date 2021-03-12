package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.models.chart.ChartMatch;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by 784420 on 10/18/2019 2:34 PM
 */
@Data
public class ChartAggregation {
    private List<ChartMatch> matches;
    private List<String> groupBy;
    private List<String> projection;
    private List<String> excludeFields;
    private LinkedHashMap<String, Object> projectionWithAlias;
    private LinkedHashMap<String, String> sort;
    //private String dateValue; // Not Used
    private int level;
    //private int limit;
}
