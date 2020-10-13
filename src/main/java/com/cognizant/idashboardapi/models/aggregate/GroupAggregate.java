package com.cognizant.idashboardapi.models.aggregate;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class GroupAggregate {
    @NotEmpty(message = "Name should not be empty/null")
    private String name;
    @NotEmpty(message = "Groups should not be empty/null")
    @Valid
    private List<FieldAggregateGroup> groups;
}
