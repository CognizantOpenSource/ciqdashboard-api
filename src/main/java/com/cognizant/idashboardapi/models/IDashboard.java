package com.cognizant.idashboardapi.models;

import com.cognizant.idashboardapi.base.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "dashboards")
public class IDashboard extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    private String name;
    @NotBlank(message = "ProjectName should not be empty/null")
    @Size(min = 4, message = "ProjectName minimum characters should be '4' ")
    private String projectName;
    private boolean active;
    private Boolean openAccess = false;
    private List<IDPageConfig> pages;
}
