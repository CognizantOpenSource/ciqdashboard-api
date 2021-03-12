package com.cognizant.ciqdashboardapi.models.chart;

import lombok.Data;

/**
 * Created by 784420 on 9/26/2019 6:30 PM
 */
@Data
public class ChartTemplate {
    private String title;
    private String subTitle;
    private TooltipTemplate tooltip;
}
