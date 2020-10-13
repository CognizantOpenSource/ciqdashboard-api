package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.IDashboardDataSource;
import com.cognizant.idashboardapi.services.IDashboardDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/data-sources")
public class DataSourceController {
    @Autowired
    IDashboardDataSourceService service;

    @GetMapping
    @ResponseStatus(OK)
    public List<IDashboardDataSource> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public IDashboardDataSource get(@PathVariable String id){
        return service.assertAndGet(id);
    }

    @GetMapping("/search")
    @ResponseStatus(OK)
    public IDashboardDataSource getByName(@RequestParam String name){
        return service.assertAndGetByName(name);
    }

    @PostMapping
    @Validated
    @ResponseStatus(CREATED)
    public IDashboardDataSource add(@Valid @RequestBody IDashboardDataSource dashboardDataSource){
        return service.add(dashboardDataSource);
    }

    @PutMapping
    @Validated
    @ResponseStatus(OK)
    public IDashboardDataSource update(@Valid @RequestBody IDashboardDataSource dashboardDataSource){
        return service.update(dashboardDataSource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteById(@PathVariable String id){
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void deleteByIdIn(@RequestParam List<String> ids){
        service.deleteByIdIn(ids);
    }

}
