package com.cognizant.ciqdashboardapi.models.aggregate;

import com.cognizant.ciqdashboardapi.models.Type;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FieldAggregateGroup {
    private String name;
    @NotEmpty(message = "Aggregates should not be empty/null")
    @Valid
    private List<FieldAggregate> aggregates;
    @NotNull(message = "Operator should not be null")
    private Type.MathOperator operator;
}
