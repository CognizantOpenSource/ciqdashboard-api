package com.cognizant.ciqdashboardapi.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

class CommonTest {

    @Test
    void dateTest(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 00);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.getTime();
        calendar.getTimeZone();
        calendar.get(Calendar.DAY_OF_WEEK);  // Sunday to Saturday default week
        WeekFields.of(Locale.ENGLISH).getFirstDayOfWeek();  // Sunday
        WeekFields.of(Locale.ENGLISH).getMinimalDaysInFirstWeek();
        calendar.getTime();
        calendar.getFirstDayOfWeek();  // 1

        // current week
        Calendar firstDayOfWeek = (Calendar) calendar.clone();
        firstDayOfWeek.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());   // to get first day of current week
        firstDayOfWeek.getTime();

        //current month
        Calendar firstDayOfMonth = (Calendar) calendar.clone();
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);   // to get first day of current month
        firstDayOfMonth.getTime();

        // current year
        Calendar firstDayOfYear = (Calendar) calendar.clone();
        firstDayOfYear.set(Calendar.DAY_OF_YEAR, 1);   // to get first day of current year
        firstDayOfYear.getTime();

        // last week
        Calendar lastWeekStartDay = (Calendar) calendar.clone();
        int i = lastWeekStartDay.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        lastWeekStartDay.add(Calendar.DAY_OF_WEEK, -(i+7));   // to get first day of last week
        lastWeekStartDay.getTime();
        lastWeekStartDay.set(Calendar.DAY_OF_WEEK, 7);       // to get last day of last week
        lastWeekStartDay.getTime();

        // last month
        Calendar lastMonthStartDay = (Calendar) calendar.clone();
        lastMonthStartDay.add(Calendar.MONTH, -1);   // to get first day of last Month
        lastMonthStartDay.set(Calendar.DAY_OF_MONTH, 1);
        lastMonthStartDay.getTime();
        lastMonthStartDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        lastMonthStartDay.set(Calendar.DAY_OF_MONTH, lastMonthStartDay.getActualMaximum(Calendar.DAY_OF_MONTH)); // to get last day of last Month
        lastMonthStartDay.getTime();

        // last year
        Calendar lastYearStartDay = (Calendar) calendar.clone();
        lastYearStartDay.add(Calendar.YEAR, -1);   // to get first day of last Month
        lastYearStartDay.set(Calendar.DAY_OF_YEAR, 1);
        lastYearStartDay.getTime();
        lastYearStartDay.set(Calendar.DAY_OF_YEAR, lastYearStartDay.getActualMaximum(Calendar.DAY_OF_YEAR));
        lastYearStartDay.getTime();

        // last N month
        Calendar lastMonthEndDay = (Calendar) calendar.clone();
        lastMonthEndDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        lastMonthEndDay.set(Calendar.DAY_OF_MONTH, lastMonthEndDay.getActualMaximum(Calendar.DAY_OF_MONTH)); // to get last day of last Month
        lastMonthEndDay.getTime();
        Calendar lastNMonthStartDay = (Calendar) calendar.clone();
        lastNMonthStartDay.add(Calendar.MONTH, -1*5);   // to get first day of last N Month
        lastNMonthStartDay.set(Calendar.DAY_OF_MONTH, 1);
        lastNMonthStartDay.getTime();
        Assertions.assertNotNull(calendar);
    }

    @Test
    void doubleConversion(){
        Object o = 1;
        Object o1 = 11.2D;
        Object o2 = 14.4F;
        Object o3 = 13L;
        Assertions.assertNotNull(o);
    }
}
