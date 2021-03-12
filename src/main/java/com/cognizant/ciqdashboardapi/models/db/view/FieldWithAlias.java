package com.cognizant.ciqdashboardapi.models.db.view;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class FieldWithAlias {
    @NotEmpty(message = "FieldName should not be null/empty")
    private String name;
    private String alias;

    public String getAlias() {
        return null==alias? name : alias;
    }
}
