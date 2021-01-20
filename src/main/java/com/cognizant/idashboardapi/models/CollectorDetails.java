package com.cognizant.idashboardapi.models;

import com.cognizant.idashboardapi.base.models.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Document(collection = "collector-details")
@EqualsAndHashCode(callSuper = false)
public class CollectorDetails extends BaseModel {
    private String id;
    @NotBlank(message = "CollectorDetails name should not be empty/null")
    @Size(min = 4, message = "CollectorDetails name should be minimum 4 characters")
    @Indexed(name = "collection-details-name-index", unique = true)
    private String name;
    private String collectionName;
    //private String info;
}
