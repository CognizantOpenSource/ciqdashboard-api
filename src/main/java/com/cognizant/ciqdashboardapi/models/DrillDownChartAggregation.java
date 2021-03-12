package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by 784420 on 10/18/2019 2:34 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DrillDownChartAggregation extends GroupByAggregation {
    @JsonIgnore
    private int level;
}
