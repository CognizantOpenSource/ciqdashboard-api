package com.cognizant.idashboardapi.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ComboChartGroup {
    private List<FilterConfig> filters;
    private List<String> groupBy;
    @NotBlank(message = "Type should not be empty/null")
    @Size(min = 3, message = "Type minimum characters should be '3' ")
    private String type;
}
