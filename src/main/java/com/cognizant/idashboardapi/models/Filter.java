package com.cognizant.idashboardapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private String field;
    private OPType op;
    private Object value;
    private Object maxValue;

    public enum OPType {
        eq, equals, ne,
        gt, gte, lt, lte, between,
        in, nin,
        contains, startswith, endswith, notcontains,
        matches, regex,
        lastNYear, lastNMonth, lastNWeek, lastNDay,
        thisYear, thisMonth, thisWeek, thisDay
    }

    public Filter(String field, OPType op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }
}
