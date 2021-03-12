package com.cognizant.ciqdashboardapi.chart;

import com.cognizant.ciqdashboardapi.models.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ChartItemTest {

    @Test
    void chartTest(){
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType("pie-chart");
        Type.ChartItemType chartItemType = optional.get();
        Type.GenericChartItemType genericChartItemType = Type.getGenericChartItemType(chartItemType);
       Assertions.assertNotNull(genericChartItemType);
    }
}
