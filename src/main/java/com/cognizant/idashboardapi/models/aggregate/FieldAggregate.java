package com.cognizant.idashboardapi.models.aggregate;

import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.models.Type;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FieldAggregate {
    @NotEmpty(message = "Field should not be empty/null")
    private String field;
    private Type.AggregateType type;
    private List<FilterConfig> filters;
    @NotNull(message = "Operator should not be null")
    private Type.MathOperator operator;
    private Double value = 0D;
}
