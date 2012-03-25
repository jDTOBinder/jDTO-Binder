/*
 *    Copyright 2011 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.annotation;

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
 * @author Juan Alberto Lopez Cavallotti
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
     * @return the type of the cascaded DTO that should be generated.
     */
    Class targetType() default Object.class;
}
