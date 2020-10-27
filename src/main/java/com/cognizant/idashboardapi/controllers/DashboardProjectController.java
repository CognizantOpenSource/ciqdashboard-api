package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.models.IDashboardProject;
import com.cognizant.idashboardapi.services.IDashboardProjectService;
import com.cognizant.idashboardapi.services.IDashboardService;
import com.cognizant.idashboardapi.services.ProjectMappingService;
import com.cognizant.idashboardapi.services.UserValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Slf4j
public class DashboardProjectController {

    private IDashboardProjectService service;
    private IDashboardService dashboardService;
    private ProjectMappingService projectMappingService;
    private UserValidationService userValidationService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','idashboard.project.view')")
    public List<IDashboardProject> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.get(getUserProjectIds());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#id, 'Project','idashboard.project.view')")
    public IDashboardProject get(@PathVariable String id) {
        return service.getOrAssert(id);
    }

    @GetMapping("/{id}/dashboards")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#id, 'Project','idashboard.project.view')")
    public List<IDashboard> getDashboards(@PathVariable String id) {
        IDashboardProject iDashboardProject = get(id);
        return dashboardService.getByProjectName(iDashboardProject.getName());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#projectId, 'Project','idashboard.project.create')")
    public IDashboardProject insert(@Valid @RequestBody IDashboardProject project) {
        service.assertNotExists(project);
        IDashboardProject dashboardProject = service.insert(project);
        projectMappingService.insert(dashboardProject.getId());
        return dashboardProject;
    }

    @PutMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','idashboard.project.update')")
    public IDashboardProject update(@Valid @RequestBody IDashboardProject project) {
        return service.update(project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#id, 'Project','idashboard.project.delete')")
    public void delete(@PathVariable String id) {
        Optional<IDashboardProject> dashboardProject = service.get(id);
        if (dashboardProject.isPresent()) {
            dashboardService.deleteByProjectName(dashboardProject.get().getName());
            projectMappingService.delete(dashboardProject.get().getId());
            service.delete(id);
        }
    }

    private List<String> getUserProjectIds() {
        return userValidationService.getCurrentUserProjectIds();
    }

}
