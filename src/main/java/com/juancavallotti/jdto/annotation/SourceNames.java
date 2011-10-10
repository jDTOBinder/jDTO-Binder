package com.juancavallotti.jdto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type to give names to the source beans in a multi-source dto context.
 * The names for the value array will be taken in the same order as the source
 * beans are called on varargs methods. <br />
 * 
 * If this annotation is used at type level, it will serve as the default mapping.
 * This mapping will be overriden by any method annotated with this type.
 * 
 * @author juancavallotti
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceNames {
    
    /**
     * The names in order of the source beans. <br />
     * @return 
     */
    String[] value();
}
