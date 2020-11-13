package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.ProjectMapping;
import com.cognizant.idashboardapi.services.ProjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/project-mapping")
public class ProjectMappingController {
    @Autowired
    ProjectMappingService service;

    @GetMapping("/projects/{projectId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public ProjectMapping get(@PathVariable String projectId){
        return service.assertOrGet(projectId);
    }

    @GetMapping("/projects")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<ProjectMapping> getAll(){
        return service.getAll();
    }

    @PostMapping("/projects/{projectId}")
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public ProjectMapping insert(@PathVariable String projectId){
        return service.insert(projectId);
    }

    @PutMapping("/projects/{projectId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public ProjectMapping update(@PathVariable String projectId){
        return service.update(projectId);
    }

    @DeleteMapping("/projects/{projectId}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public void delete(@PathVariable String projectId){
        service.delete(projectId);
    }

    /*Users*/
    @GetMapping("/users/{userId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<String> getUserProjectIds(@PathVariable String userId){
        return service.getUserProjectIds(userId);
    }

    @GetMapping("/users/current-user")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<String> getCurrentUserProjectIds(){
        return service.getCurrentUserProjectIds();
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<ProjectMapping> updateWithUser(@PathVariable String userId, @RequestBody List<String> projectIds){
        return service.updateWithUser(userId, projectIds);
    }

    /*Teams*/
    @PutMapping("/teams/{teamId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<ProjectMapping> updateWithTeamId(@PathVariable String teamId, @RequestBody List<String> projectIds){
        return service.updateWithTeams(teamId, projectIds);
    }

    @GetMapping("/teams/{teamId}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public List<String> getByTeamId(@PathVariable String teamId){
        return service.getByTeamName(teamId);
    }

    @DeleteMapping("/teams/{teamId}/projects/{projectId}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#projectId, 'ProjectMapping','idashboard.project.update')")
    public void deleteTeamIdFromProject(@PathVariable String teamId, @PathVariable String projectId){
        service.deleteTeamIdFromProject(projectId, teamId);
    }

}
