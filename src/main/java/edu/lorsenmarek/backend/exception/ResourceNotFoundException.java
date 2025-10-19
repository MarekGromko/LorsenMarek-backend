package edu.lorsenmarek.backend.exception;

import lombok.Getter;

/**
 * {@link RuntimeException} thrown when a required resource could not be found
 */
@Getter
public class ResourceNotFoundException extends RuntimeException{
    /** Some information about the type of resource requested */
    final private String resourceType;
    /** Some URI used to locate the resource */
    final private String resourceId;
    /**
     * Create a new {@link ResourceNotFoundException} given a resource type
     * The resource id will be set to <code>null</code>
     * @param resourceType the type of resource
     */
    public ResourceNotFoundException(String resourceType) {
        super("Resource not found: %s".formatted(resourceType));
        this.resourceType = resourceType;
        resourceId = "";
    }
    /**
     * Create a new {@link ResourceNotFoundException} given a resource type and id
     * @param resourceType the type of resource
     * @param resourceId the id of the resource
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("Resource not found: %s (%s)".formatted(resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
}
