package com.example.lms.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String resourceField, String resourceValue) {
        super(resourceName+" with "+ resourceField +" of value "+resourceValue+" not found.");
    }
}
