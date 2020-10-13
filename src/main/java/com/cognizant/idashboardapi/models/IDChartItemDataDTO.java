package com.cognizant.idashboardapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class IDChartItemDataDTO extends IDChartItem {
    private Object data;

    @JsonIgnore
    public void setChartItemDetails(IDChartItem chartItem){
        BeanUtils.copyProperties(chartItem, this);
    }
}
