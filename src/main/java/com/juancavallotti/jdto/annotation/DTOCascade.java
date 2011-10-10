
package com.juancavallotti.jdto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type to indicate that the field of a given dto is also a DTO. This
 * annotation can be combined with the {@link Source} annotation. <br />
 * 
 * <br />
 * If this annotation is present in combination with a {@link Source} annotation
 * or a {@link Sources} annotation, then the {@link Source#merger() } attribute
 * will be ignored.
 * 
 * @author juancavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DTOCascade {
    
    /**
     * The type of the target DTO, useful when the type cannot be infered by
     * type inspection or type parameters inspection (in the case of parameterized
     * collections). <br />
     * 
     * If the value is null or {@link  Object}.class then the type will be inferred
     * from the type of the field.
     * @return 
     */
    Class targetType() default Object.class;
}
