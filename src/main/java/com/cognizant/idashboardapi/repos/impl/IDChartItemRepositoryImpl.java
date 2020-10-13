package com.cognizant.idashboardapi.repos.impl;

import com.cognizant.idashboardapi.models.IDChartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IDChartItemRepositoryImpl {
    @Autowired
    MongoTemplate template;

    public List<IDChartItem> findByNameLikeIgnoreCase(List<String> names) {
        Query query = new Query();
        List<Criteria> list = new ArrayList<>();
        names.forEach(name -> {
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("description").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("source").regex(String.format(".*%s.*", name), "i")
                    , Criteria.where("options.title").regex(String.format(".*%s.*", name), "i")
            );
            list.add(criteria);
        });
        Criteria criteria = new Criteria().orOperator(list.toArray(new Criteria[list.size()]));
        query.addCriteria(criteria);
        query.limit(20);
        return template.find(query, IDChartItem.class);
    }
}
