package com.cognizant.idashboardapi.models;

import com.cognizant.idashboardapi.base.models.BaseModel;
import com.cognizant.idashboardapi.models.aggregate.GroupAggregate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "chartItems")
public class IDChartItem extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    private String name;
    private String description;
    private List<FilterConfig> filters;
    private List<String> groupBy;
    private List<String> projection;
    @NotBlank(message = "Type should not be empty/null")
    @Size(min = 3, message = "Type minimum characters should be '3' ")
    private String type;
    private GroupAggregate aggregate;
    private Map<String, ComboChartGroup> comboGroupBy;
    private String sourceGroup;
    private String itemGroup;
    @NotBlank(message = "Source should not be empty/null")
    @Size(min = 3, message = "Source minimum characters should be '3' ")
    private String source;
    @NotEmpty(message = "Options details should not be empty/null")
    private Map<String, Object> options;
}
