package com.juancavallotti.jdto.annotation;

import com.juancavallotti.jdto.SinglePropertyValueMerger;
import com.juancavallotti.jdto.mergers.IdentityPropertyValueMerger;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type to bind the target attribute of a DTO with a source attrbiute. <br />
 * The value for this annotation may be a nested property and the default behavior
 * will be delegated to the underlying {@link com.juancavallotti.jdto.BeanModifier} implementation.
 * 
 * @author juancavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Source {

    /**
     * The name of a property (or nested property) which will act as the source value. <br />
     * Please note that if there is a type mismatch over the property types then
     * it's up to the {@link com.juancavallotti.jdto.SinglePropertyValueMerger} implementation
     * to decide how it will be converted. Normally on a type mismatch a {@link ClassCastException}
     * is the most probable result.
     */
    String value();

    /**
     * The name of the source bean for bean matching. <br />
     * See the {@link SourceBeans} annotation for further reference on how 
     * multiple beans will be handled.
     * @return the default source bean is an empty string, the first bean specified
     * on the {@link SourceBeans} annotation will be considered as default in
     * addition to the name given by the user.
     */
    String sourceBean() default "";

    /**
     * The property merger used to merge this property. The default behavior
     * is the {@link IdentityPropertyValueMerger} which basically does not change the object.
     * @return 
     */
    Class<? extends SinglePropertyValueMerger> merger() default IdentityPropertyValueMerger.class;

    /**
     * Extra parameters to send to the value Merger instance.
     * @return 
     */
    String mergerParam() default "";
}