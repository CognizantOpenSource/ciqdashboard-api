package com.cognizant.ciqdashboardapi.models.db.view;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BaseCollectionDetailsForView {
    @NotEmpty(message = "CollectionName should not be null/empty")
    private String name;
    @NotEmpty(message = "Fields should not be null/empty")
    private List<FieldWithAlias> fields;
}
