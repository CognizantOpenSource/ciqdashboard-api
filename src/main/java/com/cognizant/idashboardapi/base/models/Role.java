package com.cognizant.idashboardapi.base.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * Created by 784420 on 7/19/2019 4:01 PM
 */
@Data
@Document(collection = "roles")
public class Role {
    @Id
    private String name;
    //private String desc;
    private List<Permission> permissions;

   //@JsonIgnore
    //@CreatedBy
    //private String user;
    //@JsonIgnore
    //@CreatedDate
    //private Instant createdDate;
   //@JsonIgnore
    //@LastModifiedBy
    //private String lastModifiedUser;
   // @JsonIgnore
    //@LastModifiedDate
    //private Instant lastModifiedDate;
}
