package com.cognizant.idashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMapping {
    @Id
    private String projectId;
    @JsonIgnore
    private Type type = Type.I_DASHBOARD_API;
    private String ownerId;
    private List<String> userIds;
    private List<String> teamIds = new ArrayList<>();

    public ProjectMapping(String projectId, String ownerId, List<String> userIds, List<String> teamIds) {
        this.projectId = projectId;
        this.ownerId = ownerId;
        this.userIds = userIds;
        this.teamIds = teamIds;
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
