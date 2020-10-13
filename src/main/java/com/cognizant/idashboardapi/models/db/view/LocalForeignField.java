package com.cognizant.idashboardapi.models.db.view;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LocalForeignField {
    @NotEmpty(message = "LocalField should not be null/empty")
    private String localField;
    @NotEmpty(message = "ForeignField should not be null/empty")
    private String foreignField;
    private OPType opType = OPType.eq;

    public enum OPType{
        eq, in
    }
}
