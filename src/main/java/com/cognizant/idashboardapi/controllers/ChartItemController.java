package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.models.IDChartItem;
import com.cognizant.idashboardapi.models.IDChartItemDataDTO;
import com.cognizant.idashboardapi.services.IDChartItemService;
import com.cognizant.idashboardapi.services.IDashboardDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/items")
public class ChartItemController {

    @Autowired
    IDChartItemService service;
    @Autowired
    IDashboardDataSourceService iDashboardDataSourceService;

    @GetMapping
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public List<IDChartItem> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public IDChartItem get(@PathVariable String id) {
        return service.assertAndGet(id);
    }

    /*
    * Commented for the future implementation
    * */
    /*@GetMapping("/search")*/
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public List<IDChartItem> searchByString(@RequestParam String searchString) {
        return service.searchByString(searchString);
    }

    @GetMapping("/search/name")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public List<IDChartItem> searchByNames(@RequestParam List<String> names) {
        return service.searchByNames(names);
    }

    @PostMapping("/{id}/chart-data")
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public IDChartItemDataDTO getChartData(@PathVariable String id, @RequestBody(required = false) Optional<List<FilterConfig>> filters) {
        return service.getChartData(id, filters);
    }

    @PostMapping("/preview")
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.view')")
    public IDChartItemDataDTO preview(@Valid @RequestBody IDChartItem chartItem) {
        iDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.preview(chartItem);
    }

    @PostMapping
    @Validated
    @ResponseStatus(CREATED)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.create')")
    public IDChartItem add(@Valid @RequestBody IDChartItem chartItem) {
        if (!"#none".equalsIgnoreCase(chartItem.getSource()))
            iDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.add(chartItem);
    }

    @PutMapping
    @Validated
    @ResponseStatus(OK)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.update')")
    public IDChartItem update(@Valid @RequestBody IDChartItem chartItem) {
        if (!"#none".equalsIgnoreCase(chartItem.getSource()))
            iDashboardDataSourceService.assertAndGetByName(chartItem.getSource());
        return service.update(chartItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.delete')")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasPermission(#chartItemId, 'ChartItem','idashboard.chart.delete')")
    public void deleteByIdIn(@RequestParam List<String> ids) {
        service.deleteByIdIn(ids);
    }


}
