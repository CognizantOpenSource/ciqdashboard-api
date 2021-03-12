package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class DataGridChartData {
    private List<DataGridHeaderCell> header;
    private List<DataGridRow> rows;

    public String toString() {
        return String.format("DataGrid(header=%s, rows=%s)", header, rows);
    }

    public static class DataGridChartDataBuilder {
        private List<DataGridHeaderCell> header;
        private List<DataGridRow> rows;

        public DataGridChartDataBuilder chardataheader(@NonNull DataGridHeaderCell... header) {
            this.header = Arrays.asList(header);
            return this;
        }

        public DataGridChartData chartdatarows(@NonNull Map... rows) {
            this.rows = Arrays.asList(rows).stream().map(DataGridRow::new).collect(Collectors.toList());
            return this.build();
        }

        public DataGridChartData chartdatarows(@NonNull List<DataGridRow> rows) {
            this.rows = rows;
            return this.build();
        }
    }
}
