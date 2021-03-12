package com.cognizant.ciqdashboardapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldType {
    private String name;
    private String type;
    private String label;
}
