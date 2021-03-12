package com.cognizant.ciqdashboardapi.errors;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("Invalid authentication details");
    }
}
