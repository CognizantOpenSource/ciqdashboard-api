/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.CIQDashboard;
import com.cognizant.ciqdashboardapi.models.CIQDashboardProject;
import com.cognizant.ciqdashboardapi.services.CIQDashboardProjectService;
import com.cognizant.ciqdashboardapi.services.CIQDashboardService;
import com.cognizant.ciqdashboardapi.services.ProjectMappingService;
import com.cognizant.ciqdashboardapi.services.UserValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/**
 * DashboardProjectController
 * @author Cognizant
 */

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Slf4j
public class DashboardProjectController {

    private CIQDashboardProjectService service;
    private CIQDashboardService dashboardService;
    private ProjectMappingService projectMappingService;
    private UserValidationService userValidationService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','ciqdashboard.project.view')")
    public List<CIQDashboardProject> getAll() {
        if (userValidationService.isAdmin())
            return service.getAll();
        return service.get(getUserProjectIds());
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.view')")
    public CIQDashboardProject get(@PathVariable String id) {
        return service.getOrAssert(id);
    }

    @GetMapping("/{id}/dashboards")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.view')")
    public List<CIQDashboard> getDashboards(@PathVariable String id) {
        CIQDashboardProject ciqDashboardProject = get(id);
        return dashboardService.getByProjectName(ciqDashboardProject.getName());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#projectId, 'Project','ciqdashboard.project.create')")
    public CIQDashboardProject insert(@Valid @RequestBody CIQDashboardProject project) {
        service.assertNotExists(project);
        CIQDashboardProject dashboardProject = service.insert(project);
        projectMappingService.insert(dashboardProject.getId());
        return dashboardProject;
    }

    @PutMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#projectId, 'Project','ciqdashboard.project.update')")
    public CIQDashboardProject update(@Valid @RequestBody CIQDashboardProject project) {
        return service.update(project);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#id, 'Project','ciqdashboard.project.delete')")
    public void delete(@PathVariable String id) {
        Optional<CIQDashboardProject> dashboardProject = service.get(id);
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
