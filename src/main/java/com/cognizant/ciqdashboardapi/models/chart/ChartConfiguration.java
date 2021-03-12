package com.cognizant.ciqdashboardapi.models.chart;

import com.cognizant.ciqdashboardapi.models.ChartAggregation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ChartConfiguration extends IDBChart {
    private boolean enabled;
    private ChartAggregation aggregation;
}
