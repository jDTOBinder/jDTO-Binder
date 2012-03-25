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
 * Annotation type to give names to the source beans in a multi-source dto context.
 * The names for the value array will be taken in the same order as the source
 * beans are called on varargs methods. <br />
 * 
 * If this annotation is used at type level, it will serve as the default mapping.
 * This mapping will be overriden by any method annotated with this type.
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceNames {
    
    /**
     * The names in order of the source beans. <br />
     * @return a list of bean names to be matched in order of appearance with 
     * source objects.
     */
    String[] value();
}
