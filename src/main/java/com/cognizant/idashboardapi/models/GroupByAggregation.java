package com.cognizant.idashboardapi.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupByAggregation extends FilterAggregation {
    private List<String> groupBy;
}
