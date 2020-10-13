package com.cognizant.idashboardapi.errors;

public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException(String type, String name) {
        super(String.format("%s (%s) already exists!", type, name));
    }

    public ResourceExistsException(String type, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s : %s", type, fieldName, fieldValue));
    }
}
