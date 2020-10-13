package com.cognizant.idashboardapi.common;

import org.junit.jupiter.api.Test;

public class CommonTest {

    @Test
    void doubleConversion(){
        Object o = 1;
        Object o1 = 11.2D;
        Object o2 = 14.4F;
        Object o3 = 13L;
        System.out.println(Double.parseDouble(o.toString()));
    }
}
