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

import org.jdto.SinglePropertyValueMerger;
import org.jdto.mergers.IdentityPropertyValueMerger;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type to bind the target attribute of a DTO with a source attrbiute. <br />
 * The value for this annotation may be a nested property and the default behavior
 * will be delegated to the underlying {@link org.jdto.BeanModifier} implementation. <br />
 * 
 * Here you can see an example of how to format a date:
 * 
 * <!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:auto;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%"><span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span>value <span style="color: #303030">=</span> <span style="background-color: #fff0f0">&quot;someDate&quot;</span><span style="color: #303030">,</span>  merger<span style="color: #303030">=</span>DateFormatMerger<span style="color: #303030">.</span><span style="color: #0000C0">class</span><span style="color: #303030">,</span> mergerParam<span style="color: #303030">=</span><span style="background-color: #fff0f0">&quot;yyyy/MM/dd&quot;</span><span style="color: #303030">)</span>
 * <span style="color: #008000; font-weight: bold">private</span> String formattedDate<span style="color: #303030">;</span></pre></div>
 * 
 * In this case, the source field must be of date or calendar type.
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Source {

    /**
     * The name of a property (or nested property) which will act as the source value. <br />
     * Please note that if there is a type mismatch over the property types then
     * it's up to the {@link org.jdto.SinglePropertyValueMerger} implementation
     * to decide how it will be converted. Normally on a type mismatch a {@link ClassCastException}
     * is the most probable result.
     */
    String value();

    /**
     * The name of the source bean for bean matching. <br />
     * See the {@link org.jdto.annotation.SourceNames} annotation 
     * for further reference on how multiple beans will be handled.
     * @return the default source bean is an empty string, the first bean specified
     * on the {@link org.jdto.annotation.SourceNames} annotation 
     * will be considered as default in addition to the name given by the user.
     */
    String sourceBean() default "";

    /**
     * The property merger used to merge this property. The default behavior
     * is the {@link IdentityPropertyValueMerger} which basically does not change the object.
     * @return the type that implements a single property value merger.
     */
    Class<? extends SinglePropertyValueMerger> merger() default IdentityPropertyValueMerger.class;

    /**
     * Extra parameters to send to the value Merger instance.
     * @return an array of parameters that will be passed to a merger object.
     */
    String[] mergerParam() default "";
}