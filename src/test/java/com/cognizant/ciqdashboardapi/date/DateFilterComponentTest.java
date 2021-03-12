package com.cognizant.ciqdashboardapi.date;

import com.cognizant.ciqdashboardapi.models.Filter;
import com.cognizant.ciqdashboardapi.services.DateFilterComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateFilterComponentTest {
    @Autowired
    DateFilterComponent component;

    @Test
    void lastNDateFilter(){
        Filter lastNDay = new Filter("test", Filter.OPType.lastNDay,10, 0);
        Filter lastNDayFilter = component.getMinAndMaxDate(lastNDay);
        Filter lastNWeek = new Filter("test", Filter.OPType.lastNWeek,3, 0);
        Filter lastNWeekFilter = component.getMinAndMaxDate(lastNWeek);
        Filter lastNMonth = new Filter("test", Filter.OPType.lastNMonth,2, 0);
        Filter lastNMonthFilter = component.getMinAndMaxDate(lastNMonth);
        Filter lastNYear = new Filter("test", Filter.OPType.lastNYear,2, 0);
        Filter lastNYearFilter = component.getMinAndMaxDate(lastNYear);
        Assertions.assertNotNull(component);
    }

    @Test
    void currentDateFilter(){
        Filter thisDay = new Filter("test", Filter.OPType.thisDay,0, 0);
        Filter thisDayFilter = component.getMinAndMaxDate(thisDay);
        Assertions.assertNotNull(component);
        Filter currentWeek = new Filter("test", Filter.OPType.thisWeek,0, 0);
        Filter currentWeekFilter = component.getMinAndMaxDate(currentWeek);
        Filter currentMonth = new Filter("test", Filter.OPType.thisMonth,0, 0);
        Filter currentMonthFilter = component.getMinAndMaxDate(currentMonth);
        Filter currentYear = new Filter("test", Filter.OPType.thisYear,0, 0);
        Filter currentYearFilter = component.getMinAndMaxDate(currentYear);
        Assertions.assertNotNull(component);
    }
}
