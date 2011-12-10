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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use the annotated constructor to build the DTO instance. When used over a
 * default constructor (or when a default constructor is present in the class), 
 * this annotation will be ignored. If there are two or more constructors on the
 * class with this annotation present, then the first one found with the annotation
 * will be used.<br />
 * 
 * This annotation is only used to know which constructor to use so if the class
 * has only one constructor, then it doesn't need to be annotated. <br />
 * 
 * When using this feature you must annotate all of the constructor arguments
 * with &#64;{@link Source} or &#64;{@link Sources} because there's no nice
 * way of reading this information by reflection.
 * 
 * @author juancavallotti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
@Documented
public @interface DTOConstructor {
    
}
