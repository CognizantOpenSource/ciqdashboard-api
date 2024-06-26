/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.models.chart.data.ChartData;
import com.cognizant.ciqdashboardapi.models.chart.data.LinkedData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * IDChartItemDataDTO
 *
 * @author Cognizant
 */

//@Data
//@EqualsAndHashCode(callSuper = false)
//public class IDChartItemDataDTO extends IDChartItem {
//    private Object data;
//
//    @JsonIgnore
//    public void setChartItemDetails(IDChartItem chartItem){
//        BeanUtils.copyProperties(chartItem, this);
//    }
//}
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IDChartItemDataDTO extends IDChartItem {
    private Object data;
    private List<LinkedData> linkeddata;
    private List<ChartData> chartData;
    @JsonIgnore
    public void setChartItemDetails(IDChartItem chartItem) {
        BeanUtils.copyProperties(chartItem, this);
    }
}