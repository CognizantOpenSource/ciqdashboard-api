package com.cognizant.idashboardapi.errors;

public class InvalidAuthenticationException extends RuntimeException {
    public InvalidAuthenticationException() {
        super("Invalid authentication details");
    }
}
