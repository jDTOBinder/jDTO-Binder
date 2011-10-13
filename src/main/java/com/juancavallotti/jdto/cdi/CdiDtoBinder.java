package com.juancavallotti.jdto.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * A qualifier for CDI to know about the special bean prepared to work with CDI.
 * <br />
 * Use this qualifier bean in order to inject a DTOBinder on the CDI method.
 * @author juancavallotti
 * @see CdiDtoBinderBean
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface CdiDtoBinder {

}
