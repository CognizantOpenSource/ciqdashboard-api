package com.cognizant.idashboardapi.date;

import com.cognizant.idashboardapi.models.Filter;
import com.cognizant.idashboardapi.services.DateFilterComponent;
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
        System.out.println(lastNDayFilter);
        Filter lastNWeek = new Filter("test", Filter.OPType.lastNWeek,3, 0);
        Filter lastNWeekFilter = component.getMinAndMaxDate(lastNWeek);
        System.out.println(lastNWeekFilter);
        Filter lastNMonth = new Filter("test", Filter.OPType.lastNMonth,2, 0);
        Filter lastNMonthFilter = component.getMinAndMaxDate(lastNMonth);
        System.out.println(lastNMonthFilter);
        Filter lastNYear = new Filter("test", Filter.OPType.lastNYear,2, 0);
        Filter lastNYearFilter = component.getMinAndMaxDate(lastNYear);
        System.out.println(lastNYearFilter);
        Assertions.assertNotNull(component);
    }

    @Test
    void currentDateFilter(){
        Filter thisDay = new Filter("test", Filter.OPType.thisDay,0, 0);
        Filter thisDayFilter = component.getMinAndMaxDate(thisDay);
        System.out.println(thisDayFilter);
        Assertions.assertNotNull(component);
        Filter currentWeek = new Filter("test", Filter.OPType.thisWeek,0, 0);
        Filter currentWeekFilter = component.getMinAndMaxDate(currentWeek);
        System.out.println(currentWeekFilter);
        Filter currentMonth = new Filter("test", Filter.OPType.thisMonth,0, 0);
        Filter currentMonthFilter = component.getMinAndMaxDate(currentMonth);
        System.out.println(currentMonthFilter);
        Filter currentYear = new Filter("test", Filter.OPType.thisYear,0, 0);
        Filter currentYearFilter = component.getMinAndMaxDate(currentYear);
        System.out.println(currentYearFilter);
        Assertions.assertNotNull(component);
    }
}
