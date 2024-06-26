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

import lombok.Data;

import java.util.List;

/**
 * FilterConfig
 * @author Cognizant
 */

@Data
public class FilterConfig {
    private String name;
    private LogicalOperatorType logicalOperator = LogicalOperatorType.AND;
    private List<Filter> configs;
    private Boolean active;

    public enum LogicalOperatorType{
        OR, AND
    }

    public FilterConfig() {
    }

    public FilterConfig(String name, LogicalOperatorType logicalOperator, List<Filter> configs, Boolean active) {
        this.name = name;
        this.logicalOperator = logicalOperator;
        this.configs = configs;
        this.active = active;
    }
}
