/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.juancavallotti.jdto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the annotated property is transsient and therefore should not
 * be considered on the DTO binding process.
 * @author juancavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DTOTransient {
    
}
