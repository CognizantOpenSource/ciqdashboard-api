package com.cognizant.ciqdashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FilterAggregation {
    private List<FilterConfig> filters;

    @JsonIgnore
    public List<Filter> getFilterList(){
        return this.filters.stream().flatMap(filterConfig -> filterConfig.getConfigs().stream()).collect(Collectors.toList());
    }
}
