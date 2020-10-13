package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.ProjectMapping;
import com.cognizant.idashboardapi.services.ProjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ProjectMapping get(@PathVariable String projectId){
        return service.assertOrGet(projectId);
    }

    @GetMapping("/projects")
    @ResponseStatus(OK)
    public List<ProjectMapping> getAll(){
        return service.getAll();
    }

    @PostMapping("/projects/{projectId}")
    @ResponseStatus(CREATED)
    public ProjectMapping insert(@PathVariable String projectId){
        return service.insert(projectId);
    }

    @PutMapping("/projects/{projectId}")
    @ResponseStatus(OK)
    public ProjectMapping update(@PathVariable String projectId){
        return service.update(projectId);
    }

    @DeleteMapping("/projects/{projectId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable String projectId){
        service.delete(projectId);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(OK)
    public List<String> getUserProjectIds(@PathVariable String userId){
        return service.getUserProjectIds(userId);
    }

    @GetMapping("/users/current-user")
    @ResponseStatus(OK)
    public List<String> getCurrentUserProjectIds(){
        return service.getCurrentUserProjectIds();
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(OK)
    public List<ProjectMapping> updateWithUser(@PathVariable String userId, @RequestBody List<String> projectIds){
        return service.updateWithUser(userId, projectIds);
    }

}
