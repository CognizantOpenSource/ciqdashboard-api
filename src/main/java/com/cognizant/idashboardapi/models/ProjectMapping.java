package com.cognizant.idashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMapping {
    @Id
    private String projectId;
    @JsonIgnore
    private Type type = Type.EXECUTION_API;
    private String ownerId;
    private List<String> userIds;

    public ProjectMapping(String projectId, String ownerId, List<String> userIds) {
        this.projectId = projectId;
        this.ownerId = ownerId;
        this.userIds = userIds;
    }

    @JsonIgnore
    @CreatedDate
    private Date createdDate;
    @JsonIgnore
    @CreatedBy
    private String createdUser;
    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedUser;
    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;

    enum Type{
        WORKBENCH_API, DASHBOARD_API, EXECUTION_API, I_DASHBOARD_API
    }
}
