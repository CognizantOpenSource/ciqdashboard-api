package com.cognizant.ciqdashboardapi.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class IDPageConfig {
    @NotBlank(message = "PageName should not be empty/null")
    @Size(min = 3, message = "PageName minimum characters should be '3' ")
    private String name;
    private GridConfig gridConfig;
    private boolean active;
    private List<IDItemConfig> items;

    @Data
    public class GridConfig{
        private int rows;
        private int columns;
        private int margin;
    }
}
