package com.cognizant.idashboardapi.base.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Created by 784420 on 7/19/2019 6:33 PM
 */
@Data
@Document(collection = "accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String userId;
    @DBRef
    private List<Role> roles;
    private List<String> projectIds;
    private List<String> ownProjectIds;

   // @JsonIgnore
    //@CreatedBy
    //private String user;
    @JsonIgnore
    @CreatedDate
    private Instant createdDate;
    //@JsonIgnore
    //@LastModifiedBy
   //private String lastModifiedUser;
    //@JsonIgnore
    //@LastModifiedDate
    //private Instant lastModifiedDate;

    @JsonIgnore
    @Value("${app.permission.admin}")
    private String adminPermission;


    @JsonIgnore
    public boolean hasProject(String projectId) {
        return Objects.nonNull(projectIds) && projectIds.contains(projectId);
    }

    @JsonIgnore
    public boolean hasOwnProject(String projectId) {
        return Objects.nonNull(ownProjectIds) && ownProjectIds.contains(projectId);
    }

    @JsonIgnore
    public boolean hasPermission(String permissionId) {
        return Objects.nonNull(roles) && roles.stream().map(Role::getPermissions).flatMap(List::stream)
                .map(Permission::getId).anyMatch(localPermissionId -> localPermissionId.equalsIgnoreCase(permissionId));
    }

    @JsonIgnore
    public boolean isAdmin() {
        return Objects.nonNull(roles) && roles.stream().map(Role::getPermissions).flatMap(List::stream)
                .map(Permission::getId).anyMatch(localPermissionId -> localPermissionId.equalsIgnoreCase(adminPermission));
    }
}
