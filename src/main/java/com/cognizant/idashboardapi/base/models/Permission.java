package com.cognizant.idashboardapi.base.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by 784420 on 7/19/2019 4:03 PM
 */
@Data
@Document(collection = "permissions")
public class Permission {
    @Id
    @NotBlank(message = "Id should not be empty")
    @Size(min = 4, message = "Id should have at least 4 characters")
    private String id;
    //@NotBlank(message = "Name should not be empty")
    //@Size(min = 4, message = "Name should have at least 4 characters")
    //private String name;

}
