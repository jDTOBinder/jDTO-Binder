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
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
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
