/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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
 * will be ignored.<br />
 * 
 * 
 * The following example demonstrates the use of cascading:
 * 
 * <!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:auto;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%"> <span style="color: #008000; font-weight: bold">public</span> <span style="color: #008000; font-weight: bold">class</span> <span style="color: #B00060; font-weight: bold">ComplexArrayDTO</span> <span style="color: #303030">{</span>
 *   
 *   <span style="color: #505050; font-weight: bold">@DTOCascade</span>
 *   <span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span><span style="background-color: #fff0f0">&quot;sourceList&quot;</span><span style="color: #303030">)</span>
 *   <span style="color: #008000; font-weight: bold">private</span> FormatDTO<span style="color: #303030">[]</span> formatDtos<span style="color: #303030">;</span>
 *   <span style="color: #303030">...</span> <span style="color: #808080">// GETTERS AND SETTERS</span>
 * <span style="color: #303030">}</span>
 * </pre></div>
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
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
