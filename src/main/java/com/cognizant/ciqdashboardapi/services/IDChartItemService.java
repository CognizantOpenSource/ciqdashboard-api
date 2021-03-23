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

package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.FilterConfig;
import com.cognizant.ciqdashboardapi.models.IDChartItem;
import com.cognizant.ciqdashboardapi.models.IDChartItemDataDTO;
import com.cognizant.ciqdashboardapi.models.Type;
import com.cognizant.ciqdashboardapi.repos.IDChartItemRepository;
import com.cognizant.ciqdashboardapi.repos.impl.IDChartItemRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * IDChartItemService
 * @author Cognizant
 */

@Service
public class IDChartItemService {

    @Autowired
    IDChartItemRepository repository;
    @Autowired
    IDChartItemRepositoryImpl repositoryImpl;
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
