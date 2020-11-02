package com.cognizant.idashboardapi.models.chart;

import com.cognizant.idashboardapi.models.chart.data.ChartData;
import com.cognizant.idashboardapi.models.chart.data.ChartType;
import com.cognizant.idashboardapi.models.chart.data.DataGridChartData;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IDBChart {
    private String id;
    private ChartType type;
    private String name;
    private String projectName;
    private ChartProperties properties;
    private ChartTemplate template;
    private Object data;

    public void data(@NonNull ChartData... data) {
        setData(Arrays.asList(data));
    }

    public static class IDBChartBuilder {

        public IDBChart data(@NonNull ChartData... data) {
            this.data = Arrays.asList(data);
            return this.build();
        }
        public IDBChart data(@NonNull List<ChartData> data) {
            this.data = data;
            return this.build();
        }
        public IDBChart data(@NonNull DataGridChartData data) {
            this.data = data;
            return this.build();
        }
    }
}
