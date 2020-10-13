package com.cognizant.idashboardapi.models;

import java.util.Arrays;
import java.util.Optional;

import static com.cognizant.idashboardapi.models.Type.GenericChartItemType.*;

public class Type {

    public static GenericChartItemType getGenericChartItemType(ChartItemType chartItemType){
        switch (chartItemType){
            case TABLE:
                return TABLE;
            case LABEL:
            case IMAGE:
                return NONE;

            case PIE_CHART:
            case PIE_CHART_ADVANCED:
            case PIE_CHART_GRID:
            case CARD_CHART:
            case TREE_MAP_CHART:
                return DRILL_DOWN_CHART;
            
            case BAR_CHART:            
            case BAR_VERTICAL_STACKED:
            case BAR_HORIZONTAL_STACKED:
            case BAR_VERTICAL_NORMALIZED:
            case BAR_HORIZONTAL_NORMALIZED:
            case LINE_CHART_SERIES:
            case LINE_CHART:
            case POLAR_CHART:
            case AREA_CHART:
            case AREA_CHART_NORMALIZED:
            case LINE_CHART_AREA_STACKED:
            case BUBBLE_CHART:
            case BAR_VERTICAL_GROUP:
            case BAR_HORIZONTAL_GROUP:
            case HEATMAP_CHART:
                return BAR_CHART;

            case GAUGE_CHART:
            case LINER_GAUGE_CHART:
            case BAR_CHART_VERTICAL:
            case BAR_CHART_HORIZONTAL:
            default:
                return LINE;
        }
    }

    public enum GenericChartItemType{
        BAR_CHART, DRILL_DOWN_CHART, LINE, TABLE , NONE, AGGREGATE;
    }

    public enum ChartItemType{
        COMBO("combo"),
        TABLE("table"),
        LABEL("label"),
        IMAGE("image"),
        LINE_CHART_SERIES("line-chart-series") ,
        LINE_CHART("line-chart") ,
        AREA_CHART("area-chart"),
        AREA_CHART_NORMALIZED("area-chart-normalized"),
        LINE_CHART_AREA_STACKED("line-chart-area-stacked"),
        BAR_CHART_HORIZONTAL("bar-chart-horizontal"),
        BAR_HORIZONTAL_STACKED("bar-horizontal-stacked"),
        BAR_HORIZONTAL_NORMALIZED("bar-horizontal-normalized"),
        BAR_VERTICAL_NORMALIZED("bar-vertical-normalized"),
        BAR_VERTICAL_STACKED("bar-vertical-stacked"),
        BAR_CHART_VERTICAL("bar-chart-vertical"),
        BAR_CHART("bar-chart"),
        BAR_HORIZONTAL_GROUP("bar-horizontal-group"),
        BAR_VERTICAL_GROUP("bar-vertical-group"),
        PIE_CHART("pie-chart"),
        PIE_CHART_ADVANCED("pie-chart-advanced"),
        PIE_CHART_GRID("pie-chart-grid"),
        POLAR_CHART("polar-chart"),
        BUBBLE_CHART("bubble-chart"),
        HEATMAP_CHART("heatmap-chart"),
        TREE_MAP_CHART("tree-map-chart"),
        CARD_CHART("card-chart"),
        GAUGE_CHART("gauge-chart"),
        LINER_GAUGE_CHART("liner-gauge-chart") ;

        private String type;
        ChartItemType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public static Optional<ChartItemType> getChartItemType(String type){
            return Arrays.asList(values()).stream().filter(chartItemType -> chartItemType.getType().equals(type)).findAny();
        }

    }

    public enum AggregateType{
        SUM, AVG, COUNT, MIN, MAX, CONSTANT, DISTINCT_COUNT
    }

    public enum MathOperator {
        add, sub, mul, div
    }
}
