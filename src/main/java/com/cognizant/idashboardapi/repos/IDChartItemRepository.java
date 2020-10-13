package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.IDChartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IDChartItemRepository extends MongoRepository<IDChartItem, String> {
    void deleteByIdIn(List<String> ids);
}
