package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * Created by 784420 on 10/18/2019 2:34 PM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LineChartAggregation extends FilterAggregation {
    @NotEmpty(message = "GroupBy should not null/empty")
    private String groupBy;

    @JsonIgnore
    public DrillDownChartAggregation getChartAggregation(){
        DrillDownChartAggregation chartAggregation = new DrillDownChartAggregation();
        chartAggregation.setLevel(1);
        chartAggregation.setGroupBy(Arrays.asList(groupBy));
        chartAggregation.setFilters(getFilters());

        return chartAggregation;
    }

}
