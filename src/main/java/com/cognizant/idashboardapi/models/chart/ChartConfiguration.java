package com.cognizant.idashboardapi.models.chart;

import com.cognizant.idashboardapi.models.ChartAggregation;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ChartConfiguration extends IDBChart {
    private boolean enabled;
    private ChartAggregation aggregation;
}
