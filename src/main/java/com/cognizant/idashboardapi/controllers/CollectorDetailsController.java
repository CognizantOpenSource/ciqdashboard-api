package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.CollectorDetails;
import com.cognizant.idashboardapi.services.CollectorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*Commented for removing controller from Swagger*/
/*@RestController
@RequestMapping("/collector")*/
public class CollectorDetailsController {
    @Autowired
    CollectorDetailsService service;

    @GetMapping
    public List<CollectorDetails> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CollectorDetails get(@PathVariable String id) {
        return service.assertOrGet(id);
    }

    @GetMapping("/search")
    public CollectorDetails getByName(@RequestParam String name) {
        return service.assertOrGetByName(name);
    }

    @PostMapping
    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    public CollectorDetails add(@Valid @RequestBody CollectorDetails collectorDetails) {
        return service.add(collectorDetails);
    }

    @PutMapping
    @Validated
    public CollectorDetails update(@Valid @RequestBody CollectorDetails collectorDetails) {
        return service.update(collectorDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }


}
