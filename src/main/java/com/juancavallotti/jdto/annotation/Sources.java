package com.juancavallotti.jdto.annotation;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.mergers.FirstObjectPropertyValueMerger;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Combine multiple fields into only one source, then is up to the 
 * {@link MultiPropertyValueMerger} implementation to decide how to merge all into 
 * one result. This annotation is meant to be combined with the {@link Source}
 * annotation. 
 * @author juancavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Sources {

    /**
     * A group of {@link Source} annotations.
     * @return 
     */
    Source[] value();

    /**
     * The property merger used to merge this property. For now, the default behavior
     * is to find the first not null value and set it to the target.
     * @return 
     */
    Class<? extends MultiPropertyValueMerger> merger() default FirstObjectPropertyValueMerger.class;
    
    /**
     * Extra parameters to send to the value Merger instance.
     * @return 
     */
    String mergerParam() default "";
}
