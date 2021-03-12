package com.cognizant.ciqdashboardapi.models.db.view;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ViewDetails {
    @NotEmpty(message = "ViewName should not be null/empty")
    private String name;
    @NotNull(message = "BaseCollection should not be null/empty")
    @Valid
    private BaseCollectionDetailsForView baseCollection;
    @NotEmpty(message = "Lookups should not be null/empty")
    @Valid
    private List<CollectionDetailsForView> lookups;
}
