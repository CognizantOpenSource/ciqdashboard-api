package com.cognizant.ciqdashboardapi.models;

import lombok.Data;

import java.util.List;

@Data
public class AggregateConfig {
    private List<String> fields;
    private Type.AggregateType type;
}
