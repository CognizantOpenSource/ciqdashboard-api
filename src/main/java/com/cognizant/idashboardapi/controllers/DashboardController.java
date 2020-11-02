package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.services.IDashboardProjectService;
import com.cognizant.idashboardapi.services.IDashboardService;
import com.cognizant.idashboardapi.services.UserValidationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/dashboards")
@AllArgsConstructor
public class DashboardController {

    private IDashboardService service;
    private IDashboardProjectService projectService;
    private UserValidationService userValidationService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.view')")
    public List<IDashboard> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.getAllByPermissions();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.view')")
    public IDashboard get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.view')")
    public List<IDashboard> getByProjectName(@RequestParam String projectName) {
        validateProjectAccess(projectName);
        return service.getByProjectName(projectName);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.create')")
    public IDashboard add(@Valid @RequestBody IDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName());
        return service.add(dashboard);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.update')")
    public IDashboard update(@Valid @RequestBody IDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName());
        validateUpdateOpenAccess(dashboard, service.assertAndGet(dashboard.getId()));
        return service.update(dashboard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','idashboard.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }

    private void validateProjectAccess(String projectName) {
        projectService.assertAndGetByName(projectName);
        if (!userValidationService.isAdmin()) {
            List<String> names = userValidationService.getCurrentUserProjectNames();
            if (!names.contains(projectName)) {
                throw new AccessDeniedException(String.format("Project access not found with projectName:%s", projectName));
            }
        }
    }

    private void validateUpdateOpenAccess(IDashboard newDashboard, IDashboard existDashboard){
        if (!userValidationService.isAdmin()){
            Boolean openAccess = newDashboard.getOpenAccess();
            if ((null == openAccess || false==openAccess)
                    && !existDashboard.getCreatedUser().equals(userValidationService.getCurrentUserEmailId())){
                throw new InvalidDetailsException("Not allowed to update openAccess for the dashboard");
            }
        }
    }


}
