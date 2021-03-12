package com.cognizant.ciqdashboardapi.controllers;

import com.cognizant.ciqdashboardapi.models.CIQDashboardDataSource;
import com.cognizant.ciqdashboardapi.services.CIQDashboardDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/data-sources")
public class DataSourceController {
    @Autowired
    CIQDashboardDataSourceService service;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public List<CIQDashboardDataSource> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public CIQDashboardDataSource get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.view')")
    public CIQDashboardDataSource getByName(@RequestParam String name) {
        return service.assertAndGetByName(name);
    }

    @PostMapping
    @Validated
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.create')")
    public CIQDashboardDataSource add(@Valid @RequestBody CIQDashboardDataSource dashboardDataSource) {
        return service.add(dashboardDataSource);
    }

    @PutMapping
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.update')")
    public CIQDashboardDataSource update(@Valid @RequestBody CIQDashboardDataSource dashboardDataSource) {
        return service.update(dashboardDataSource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#DataSourceId, 'DataSource','ciqdashboard.datasource.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }

}
