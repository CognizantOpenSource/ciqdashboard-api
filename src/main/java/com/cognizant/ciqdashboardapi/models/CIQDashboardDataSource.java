package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.base.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "ciqDashboardDataSources")
public class CIQDashboardDataSource extends BaseModel {
    private String id;
    @NotBlank(message = "Name should not be empty/null")
    @Size(min = 4, message = "Name minimum characters should be '4' ")
    @Indexed(unique = true)
    private String name;
    @NotBlank(message = "Group should not be empty/null")
    @Size(min = 3, message = "Group minimum characters should be '3' ")
    private String group;
    @NotBlank(message = "CollectionName should not be empty/null")
    @Size(min = 3, message = "CollectionName minimum characters should be '3' ")
    private String collectionName;
    private String description;
    @NotBlank(message = "ToolName should not be empty/null")
    @Size(min = 3, message = "ToolName minimum characters should be '3' ")
    private String toolName;
    private String image;
    private String imageType;
    @NotEmpty(message = "Fields list should not be empty/null")
    private List<FieldType> fields;
}
