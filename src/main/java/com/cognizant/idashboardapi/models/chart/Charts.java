package com.cognizant.idashboardapi.models.chart;

import com.cognizant.idashboardapi.models.chart.data.*;

public class Charts {

    public static ChartData.ChartDataBuilder pie() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder doughnut() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder card() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder treeMap() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder bar() {
        return ChartData.builder();
    }

    public static ChartData.ChartDataBuilder line() {
        return ChartData.builder();
    }

    public static LeapChart.LeapChartBuilder pieChart(ChartConfiguration config) {
        return pieChart(config, true);
    }

    public static LeapChart.LeapChartBuilder doughnutChart(ChartConfiguration config) {
        return pieChart(config, false);
    }

    public static LeapChart.LeapChartBuilder cardChart(ChartConfiguration config) {
        return chart(config, ChartType.card);
    }

    public static LeapChart.LeapChartBuilder createChart(ChartConfiguration config) {
        return chart(config, config.getType());
    }

    public static LeapChart.LeapChartBuilder treeMapChart(ChartConfiguration config) {
        return chart(config, ChartType.treeMap);
    }

    public static LeapChart.LeapChartBuilder barChart(ChartConfiguration config) {
        return chart(config, ChartType.verticalStakedBar);
    }

    public static LeapChart.LeapChartBuilder lineChart(ChartConfiguration config) {
        return chart(config, ChartType.lineSeries);
    }
    public static LeapChart.LeapChartBuilder dataGridChart(ChartConfiguration config) {
        return chart(config, ChartType.dataGrid);
    }
    public static DataGridChartData.DataGridChartDataBuilder dataGrid() {
        return DataGridChartData.builder();
    }
    public static DataGridHeaderCell.DataGridHeaderCellBuilder head() {
        return DataGridHeaderCell.builder();
    }

    public static DataGridHeaderCell head(String name, String label) {
        return DataGridHeaderCell.builder().name(name).label(label).build();
    }
    public static DataGridRow.DataGridRowBuilder row() {
        return DataGridRow.builder();
    }
    public static ChartData item(String name,Object value){
        return ChartData.builder().name(name).value(value).build();
    }
    private static LeapChart.LeapChartBuilder chart(ChartConfiguration config, ChartType type) {
        LeapChart.LeapChartBuilder builder = build(config);
        builder.type(type);
        return builder;
    }

    private static LeapChart.LeapChartBuilder pieChart(ChartConfiguration config, boolean isPie) {
        LeapChart.LeapChartBuilder builder = build(config);
        builder.type(ChartType.pie);
        ChartProperties properties = config.getProperties();
        if (properties != null) {
            properties.setPie(isPie);
        } else {
            properties = ChartProperties.builder().pie(isPie).build();
        }
        return builder.properties(properties);
    }

    private static LeapChart.LeapChartBuilder build(ChartConfiguration config) {
        return LeapChart.builder()
                .id(config.getId())
                .name(config.getName())
                .type(config.getType())
                .projectName(config.getProjectName())
                .template(config.getTemplate())
                .properties(config.getProperties());

    }

}
