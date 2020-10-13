package com.cognizant.idashboardapi.errors;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String type, String name) {
        super(String.format("%s (%s) Not Found!", type, name));
    }

    public ResourceNotFoundException(String type, String fieldName, String fieldValue) {
        super(String.format("%s Not Found with %s : %s", type, fieldName, fieldValue));
    }
}
