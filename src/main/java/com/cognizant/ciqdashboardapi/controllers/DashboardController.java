package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.services.CIQDashboardProjectService;
import com.cognizant.ciqdashboardapi.services.CIQDashboardService;
import com.cognizant.ciqdashboardapi.services.UserValidationService;
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

    private CIQDashboardService service;
    private CIQDashboardProjectService projectService;
    public UserValidationService userValidationService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public List<CIQDashboard> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.getAllByPermissions();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public CIQDashboard get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.view')")
    public List<CIQDashboard> getByProjectName(@RequestParam String projectName) {
        validateProjectAccess(projectName);
        return service.getByProjectName(projectName);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.create')")
    public CIQDashboard add(@Valid @RequestBody CIQDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName());
        return service.add(dashboard);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.update')")
    public CIQDashboard update(@Valid @RequestBody CIQDashboard dashboard) {
        validateProjectAccess(dashboard.getProjectName());
        validateUpdateOpenAccess(dashboard, service.assertAndGet(dashboard.getId()));
        return service.update(dashboard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DashboardId, 'Dashboard','ciqdashboard.delete')")
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

    private void validateUpdateOpenAccess(CIQDashboard newDashboard, CIQDashboard existDashboard){
        if (!userValidationService.isAdmin()){
            Boolean openAccess = newDashboard.getOpenAccess();
            if ((null == openAccess || false==openAccess)
                    && !existDashboard.getCreatedUser().equals(userValidationService.getCurrentUserEmailId())){
                throw new InvalidDetailsException("Not allowed to update openAccess for the dashboard");
            }
        }
    }


}
