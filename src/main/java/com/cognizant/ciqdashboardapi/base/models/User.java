package com.cognizant.ciqdashboardapi.base.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * Created by 784420 on 7/17/2019 3:41 PM
 */
@Data
@Document(collection = "users")
public class User {
    @JsonIgnore
    protected String password;
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed
    private String email;
    //private String image;
    private String org;
    private Date modifiedDate = new Date();
    private boolean active;
    @DBRef
    private Account account;

    //@JsonIgnore
    //@CreatedBy
    //private String createdUser;
    @JsonIgnore
    @CreatedDate
    private Date createdDate;
    //@JsonIgnore
    //@LastModifiedBy
    //private String lastModifiedUser;
    @JsonIgnore
    @LastModifiedDate
    private Instant lastModifiedDate;

    public String getName() {
        return getFirstName() + (!Objects.isNull(getLastName()) ? " " + getLastName() : "");
    }
}
