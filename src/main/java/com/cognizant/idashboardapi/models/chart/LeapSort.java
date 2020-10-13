package com.cognizant.idashboardapi.models.chart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Data
public class LeapSort {
    private SortType type;
    private List<String> fields = new ArrayList<>();

    @JsonIgnore
    public Sort.Direction getSortDirection(){
        return Sort.Direction.valueOf(type.name());
    }

    public enum SortType{
        DESC, ASC
    }
}
