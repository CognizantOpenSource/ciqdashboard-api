package com.cognizant.idashboardapi.models.chart.data;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DataGridRow extends LinkedHashMap {

    private LinkedHashMap<String, Object> meta;

    public DataGridRow(Map source) {
        this.putAll(source);
    }

    public String toString() {
        return String.format("(%s)",
                this.keySet().stream().map(k -> k + ":" + this.get(k)).collect(Collectors.joining(", ")));
    }

    public static class DataGridRowBuilder {
        private DataGridRow row = new DataGridRow();

        public DataGridRowBuilder cell(String key, Object value) {
            row.put(key, value);
            return this;
        }

        public DataGridRow build() {
            return row;
        }
    }
}
