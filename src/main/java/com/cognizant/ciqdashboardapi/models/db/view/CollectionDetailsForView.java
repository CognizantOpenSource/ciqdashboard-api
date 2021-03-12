package com.cognizant.ciqdashboardapi.models.db.view;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionDetailsForView extends BaseCollectionDetailsForView {
    private List<LocalForeignField> localForeignFields;
    private String alias;

    public String getAlias() {
        return null == alias ? getName() : alias;
    }
}
