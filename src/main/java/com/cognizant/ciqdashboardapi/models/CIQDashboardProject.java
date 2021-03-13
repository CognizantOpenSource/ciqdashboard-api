package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.base.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "ciqDashboardProjects")
public class CIQDashboardProject extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    @Indexed(unique = true)
    private String name;
    private String description;
}
