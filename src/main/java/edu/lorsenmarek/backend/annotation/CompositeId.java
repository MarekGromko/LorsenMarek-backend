package edu.lorsenmarek.backend.annotation;

import java.lang.annotation.*;

/**
 * Field annotation for Composite Ids in models
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CompositeId {}
