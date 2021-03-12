package com.cognizant.ciqdashboardapi.models.chart;

import lombok.*;

import java.util.LinkedHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ChartProperties extends LinkedHashMap {
    private boolean interactive;
    private boolean pie;
}
