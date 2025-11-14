package com.example.lms.exception;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String resourceName, String resourceField, String resourceFieldValue) {
        super(resourceName+" with "+ resourceField +" of value "+resourceFieldValue+" not found.");
    }
}
