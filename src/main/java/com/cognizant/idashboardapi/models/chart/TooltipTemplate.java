package com.cognizant.idashboardapi.models.chart;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by 784420 on 9/26/2019 6:29 PM
 */
@Data
public class TooltipTemplate {
    private ArrayList<String> messages;
    private ArrayList<String> groupView;
    private ArrayList<String> view;
}
