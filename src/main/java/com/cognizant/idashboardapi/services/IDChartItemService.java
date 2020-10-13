package com.cognizant.idashboardapi.services;

import com.cognizant.idashboardapi.client.NLPApiClient;
import com.cognizant.idashboardapi.errors.InvalidDetailsException;
import com.cognizant.idashboardapi.errors.ResourceNotFoundException;
import com.cognizant.idashboardapi.models.FilterConfig;
import com.cognizant.idashboardapi.models.IDChartItemDataDTO;
import com.cognizant.idashboardapi.models.IDChartItem;
import com.cognizant.idashboardapi.models.Type;
import com.cognizant.idashboardapi.repos.IDChartItemRepository;
import com.cognizant.idashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IDChartItemService {

    @Autowired
    IDChartItemRepository repository;
    @Autowired
    IDChartItemRepositoryImpl repositoryImpl;
    @Autowired
    NLPApiClient nlpApiClient;
    @Autowired
    ChartAggregationComponent chartAggregationComponent;

    public List<IDChartItem> getAll(){
        return repository.findAll();
    }

    public Optional<IDChartItem> get(String id){
        return repository.findById(id);
    }

    public IDChartItem assertAndGet(String id){
        Optional<IDChartItem> optional = get(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ResourceNotFoundException("ChartItem", "id", id);
        }
    }

    public IDChartItemDataDTO getChartData(String id, Optional<List<FilterConfig>> filters) {
        IDChartItem chartItem = assertAndGet(id);
        if (filters.isPresent()){
            chartItem.getFilters().addAll(filters.get());
        }
        return chartAggregationComponent.getChartAggregation(chartItem);
    }

    public List<IDChartItem> searchByString(String searchString) {
        List<String> keywords = nlpApiClient.getKeywords(searchString);
        if (CollectionUtils.isEmpty(keywords)) return new ArrayList<>();
        return searchByNames(keywords);
    }

    public List<IDChartItem> searchByNames(List<String> names) {
        return repositoryImpl.findByNameLikeIgnoreCase(names);
    }

    public IDChartItemDataDTO preview(IDChartItem chartItem) {
        assertAndGetChartItemType(chartItem.getType());
        return chartAggregationComponent.getChartAggregation(chartItem);
    }

    public IDChartItem add(IDChartItem chartItem){
        assertInsert(chartItem);
        assertAndGetChartItemType(chartItem.getType());
        return repository.insert(chartItem);
    }

    public IDChartItem update(IDChartItem chartItem){
        assertAndGet(chartItem.getId());
        assertAndGetChartItemType(chartItem.getType());
        return repository.save(chartItem);
    }

    public void deleteById(String id){
        assertAndGet(id);
        repository.deleteById(id);
    }

    public void deleteByIdIn(List<String> ids){
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(IDChartItem chartItem) {
        if (!StringUtils.isEmpty(chartItem.getId())){
            throw new InvalidDetailsException("Id should be null/empty");
        }
    }

    private Type.ChartItemType assertAndGetChartItemType(String type){
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType(type);
        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new InvalidDetailsException(String.format("invalid chart type(%s) provided", type));
        }
    }
}
