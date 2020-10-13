package com.cognizant.idashboardapi.models;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
public class DatabaseDetailsComponent {
    private Set<String> collectionNames;
}
