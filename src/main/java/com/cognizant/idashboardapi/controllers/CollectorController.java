package com.cognizant.idashboardapi.controllers;

import com.cognizant.idashboardapi.common.Constants;
import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.LeapResponse;
import com.cognizant.idashboardapi.models.*;
import com.cognizant.idashboardapi.models.chart.data.ChartData;
import com.cognizant.idashboardapi.models.chart.data.DataGridRow;
import com.cognizant.idashboardapi.models.db.view.ViewDetails;
import com.cognizant.idashboardapi.services.CollectorDetailsService;
import com.cognizant.idashboardapi.services.CollectorService;
import com.cognizant.idashboardapi.services.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/collector")
@Slf4j
public class CollectorController {
    @Autowired
    CollectorService collectorService;
    @Autowired
    CollectorDetailsService collectorDetailsService;
    @Autowired
    ValidatorService validatorService;

    @GetMapping("/collection-names")
    public Set<String> getCollectionNames() {
        return collectorService.getCollectionNames();
    }

    @GetMapping("/fields-by-collection")
    public Set<String> getFieldsByCollection(@RequestParam(name = "collection-name") String collectionName) {
        return collectorService.getFieldsByCollection(collectionName);
    }

    @GetMapping("/fields-types")
    public List<FieldType> getFieldsAndTypes(@RequestParam(name = "collection-name") String collectionName) {
        return collectorService.getFieldsAndTypes(collectionName);
    }
    @GetMapping("/distinct-values-by-field-collection")
    public Set<String> getDistinctValuesByField(@RequestParam(name = "collection-name") String collectionName,
                                                @RequestParam(name = "field-name") String fieldName) {
        return collectorService.getDistinctValuesByField(collectionName, fieldName);
    }

    @GetMapping("/distinct-values-by-field")
    public Set<String> getDistinctValuesByFieldSource(@RequestParam(name = "source-name") String sourceName,
                                                @RequestParam(name = "field-name") String fieldName) {
        return collectorService.getDistinctValuesBySourceField(sourceName, fieldName);
    }

    @PostMapping("/create-view")
    @Validated
    public ResponseEntity<LeapResponse> createView(@Valid @RequestBody ViewDetails viewDetails, HttpServletRequest request) {
        if (!viewDetails.getName().startsWith(Constants.COLLECTION_FILTER_SOURCE))
            throw new InvalidDetailsException(String.format("View name should start with '%s'", Constants.COLLECTION_FILTER_SOURCE));
        collectorService.createView(viewDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LeapResponse(LocalDateTime.now(),
                CREATED.value(),
                "",
                "View created successfully",
                request.getRequestURI()));
    }

    @PostMapping("/create-collection")
    @ResponseStatus(CREATED)
    public ResponseEntity<LeapResponse> createCustomCollection(@RequestParam String collectionName,
                                                               @RequestBody List<Map<String, Object>> collectorData,
                                                               HttpServletRequest request) {
        if (!collectionName.startsWith(Constants.COLLECTION_FILTER_SOURCE))
            throw new InvalidDetailsException(String.format("Collection name should start with '%s'", Constants.COLLECTION_FILTER_SOURCE));
        collectorService.addCustomCollection(collectionName, collectorData);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LeapResponse(LocalDateTime.now(),
                CREATED.value(),
                "",
                "Collection created successfully",
                request.getRequestURI()));

    }

    @PutMapping("/update-collection")
    @ResponseStatus(OK)
    public ResponseEntity<LeapResponse> updateCustomCollection(@RequestParam String collectionName,
                                                               @RequestBody List<Map<String, Object>> collectorData,
                                                               @RequestParam(required = false, defaultValue = "false") boolean override,
                                                               HttpServletRequest request) {
        if (!override) {
            collectorService.updateCustomCollection(collectionName, collectorData);
        } else {
            deleteView(collectionName, request);
            createCustomCollection(collectionName, collectorData, request);
        }
        return ResponseEntity.status(OK).body(new LeapResponse(LocalDateTime.now(),
                OK.value(),
                "",
                "Collection updated successfully",
                request.getRequestURI()));

    }

    @DeleteMapping("/drop-collection")
    @Validated
    public ResponseEntity<LeapResponse> deleteView(@RequestParam String collectionName, HttpServletRequest request) {
        collectorService.deleteView(collectionName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new LeapResponse(LocalDateTime.now(),
                NO_CONTENT.value(),
                "",
                "Collection/View dropped successfully",
                request.getRequestURI()));
    }

    //@PostMapping("/chart/drill-down-chart")
    public List<ChartData> getChart(@RequestBody DrillDownChartAggregation chartAggregation,
                                    @RequestParam(name = "collector-name") String collectorName) {
        CollectorDetails collectorDetails = collectorDetailsService.assertOrGetByName(collectorName);
        Set<String> fields = getFieldsByCollection(collectorDetails.getCollectionName());
        DrillDownChartAggregation validChartAggregation = validatorService.validateChartAggregation(chartAggregation, fields);
        return collectorService.getChart(validChartAggregation, collectorDetails.getCollectionName());
    }
    // @PostMapping("/chart/table")
    public List<DataGridRow> getTable(@RequestBody TableAggregation tableAggregation,
                                      @RequestParam(name = "collector-name") String collectorName) {
        CollectorDetails collectorDetails = collectorDetailsService.assertOrGetByName(collectorName);
        Set<String> fields = getFieldsByCollection(collectorDetails.getCollectionName());
        TableAggregation validTableAggregation = validatorService.validateTableAggregation(tableAggregation, fields);
        return collectorService.getTable(validTableAggregation, collectorDetails.getCollectionName());
    }
    //@PostMapping("/chart/bar-chart")
    @Validated
    public List<ChartData> getBarChart(@Valid @RequestBody BarChartAggregation chartAggregation,
                                       @RequestParam(name = "collector-name") String collectorName) {
        CollectorDetails collectorDetails = collectorDetailsService.assertOrGetByName(collectorName);
        Set<String> fields = getFieldsByCollection(collectorDetails.getCollectionName());
        BarChartAggregation validBarChartAggregation = validatorService.validateBarChartAggregation(chartAggregation, fields);
        return collectorService.getBarChart(validBarChartAggregation, collectorDetails.getCollectionName());
    }
    //@PostMapping("/chart/line-chart")
    @Validated
    public List<ChartData> getLineChart(@Valid @RequestBody LineChartAggregation lineChartAggregation,
                                        @RequestParam(name = "collector-name") String collectorName) {
        CollectorDetails collectorDetails = collectorDetailsService.assertOrGetByName(collectorName);
        Set<String> fields = getFieldsByCollection(collectorDetails.getCollectionName());
        DrillDownChartAggregation chartAggregation = validatorService.validateLineChartAggregation(lineChartAggregation.getChartAggregation(), fields);
        return collectorService.getChart(chartAggregation, collectorDetails.getCollectionName());
    }
}
