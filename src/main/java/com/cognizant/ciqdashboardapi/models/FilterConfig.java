package com.cognizant.ciqdashboardapi.models;

import lombok.Data;

import java.util.List;

@Data
public class FilterConfig {
    private String name;
    private LogicalOperatorType logicalOperator = LogicalOperatorType.AND;
    private List<Filter> configs;
    private Boolean active;

    public enum LogicalOperatorType{
        OR, AND
    }
}
