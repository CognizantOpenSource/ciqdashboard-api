package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataGridHeaderCell {
    private String name;
    private String label;

    public String toString(){
        return String.format("(name=%s, label=%s)",name,label);
    }
}
