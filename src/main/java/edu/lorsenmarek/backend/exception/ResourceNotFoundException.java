package edu.lorsenmarek.backend.exception;

import lombok.Getter;

/**
 * {@link RuntimeException} thrown when a required resource could not be found
 */
@Getter
public class ResourceNotFoundException extends RuntimeException{
    final private String resourceType;
    final private String resourceId;
    public ResourceNotFoundException(String resourceType) {
        super("Resource not found: %s".formatted(resourceType));
        this.resourceType = resourceType;
        resourceId = "";
    }
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("Resource not found: %s (%s)".formatted(resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
}
