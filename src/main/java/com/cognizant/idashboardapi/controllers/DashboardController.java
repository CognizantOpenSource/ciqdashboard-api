package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.IDashboard;
import com.cognizant.idashboardapi.services.IDashboardProjectService;
import com.cognizant.idashboardapi.services.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/dashboards")
public class DashboardController {

    @Autowired
    IDashboardService service;
    @Autowired
    IDashboardProjectService projectService;

    @GetMapping
    @ResponseStatus(OK)
    public List<IDashboard> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public IDashboard get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    public List<IDashboard> getByProjectName(@RequestParam String projectName) {
        return service.getByProjectName(projectName);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Validated
    public IDashboard add(@Valid @RequestBody IDashboard dashboard) {
        projectService.assertAndGetByName(dashboard.getProjectName());
        return service.add(dashboard);
    }

    @PutMapping
    @ResponseStatus(OK)
    @Validated
    public IDashboard update(@Valid @RequestBody IDashboard dashboard) {
        projectService.assertAndGetByName(dashboard.getProjectName());
        return service.update(dashboard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }


}
