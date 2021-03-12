package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiSeriesChartData {
    public String name;
    public Object value;
    public List<MultiSeriesChartData> series;

    public String toString() {
        if (series == null)
            return String.format("(%s:%s)", name, value);
        else
            return String.format("(%s:%s, children:%s)", name, value, series.toString());
    }

    public static class MultiSeriesChartDataBuilder {

        public List<MultiSeriesChartData> series;

        public MultiSeriesChartData series(@NonNull MultiSeriesChartData... data) {
            this.series = Arrays.asList(data);
            return this.build();
        }
    }
}
