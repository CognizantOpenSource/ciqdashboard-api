package com.cognizant.idashboardapi.models.chart.data;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartData {
    public String name;
    public Object value;
    public List<ChartData> children;
    public List<ChartData> series;

    public String toString() {
        if (children == null && series == null)
            return String.format("(%s:%s)", name, value);
        else if (series == null)
            return String.format("(%s:%s, children:%s)", name, value, children.toString());
        else
            return String.format("(%s:%s)", name, series.toString());
    }

    public static class ChartDataBuilder {

        public List<ChartData> children;
        public List<ChartData> series;

        public ChartData children(@NonNull ChartData... data) {
            this.children = Arrays.asList(data);
            return this.build();
        }

        public ChartData series(@NonNull ChartData... data) {
            this.series = Arrays.asList(data);
            return this.build();
        }

        public ChartData series(List<ChartData> data) {
            this.series = data;
            return this.build();
        }
    }
}
