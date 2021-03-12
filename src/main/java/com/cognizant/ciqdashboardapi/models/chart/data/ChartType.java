package com.cognizant.ciqdashboardapi.models.chart.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ChartType {
    verticalStakedBar("vertical-stacked-bar"),
    lineSeries("line-series"),
    pie("pie-chart"),
    treeMap("tree-map"),
    card("card"),
    dataGrid("data-grid");

    private String type;

    private ChartType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}