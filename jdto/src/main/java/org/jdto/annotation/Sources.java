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

import org.jdto.MultiPropertyValueMerger;
import org.jdto.mergers.FirstObjectPropertyValueMerger;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Combine multiple fields into only one source, then is up to the 
 * {@link MultiPropertyValueMerger} implementation to decide how to merge all into 
 * one result. This annotation is meant to be combined with the {@link Source}
 * annotation. <br />
 * 
 * The following example demonstrates the use of compound source binding: <br />
 * 
 * <!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:auto;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%"> <span style="color: #008000; font-weight: bold">public</span> <span style="color: #008000; font-weight: bold">class</span> <span style="color: #B00060; font-weight: bold">FormatDTO</span> <span style="color: #303030">{</span>
 *   
 *   <span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span>value<span style="color: #303030">=</span><span style="background-color: #fff0f0">&quot;aDouble&quot;</span><span style="color: #303030">,</span> merger<span style="color: #303030">=</span>StringFormatMerger<span style="color: #303030">.</span><span style="color: #0000C0">class</span><span style="color: #303030">,</span> 
 *       mergerParam<span style="color: #303030">=</span><span style="background-color: #fff0f0">&quot;$ %.2f&quot;</span><span style="color: #303030">)</span>
 *   <span style="color: #008000; font-weight: bold">private</span> String price<span style="color: #303030">;</span>
 *   
 *   <span style="color: #505050; font-weight: bold">@Sources</span><span style="color: #303030">(</span>value<span style="color: #303030">={</span><span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span><span style="background-color: #fff0f0">&quot;aDouble&quot;</span><span style="color: #303030">),</span> <span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span><span style="background-color: #fff0f0">&quot;anInt&quot;</span><span style="color: #303030">)},</span> 
 *           merger<span style="color: #303030">=</span>StringFormatMerger<span style="color: #303030">.</span><span style="color: #0000C0">class</span><span style="color: #303030">,</span> mergerParam<span style="color: #303030">=</span><span style="background-color: #fff0f0">&quot;%.2f %08d&quot;</span><span style="color: #303030">)</span>
 *   <span style="color: #008000; font-weight: bold">private</span> String compound<span style="color: #303030">;</span>
 *   
 *   <span style="color: #303030">...</span> <span style="color: #808080">// GETTERS AND SETTERS</span>
 * </pre></div>
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Sources {

    /**
     * A group of {@link Source} annotations.
     * @return the source value settings to be merged.
     */
    Source[] value();

    /**
     * The property merger used to merge this property. For now, the default behavior
     * is to find the first not null value and set it to the target.
     * @return the merger type to merge the source values.
     */
    Class<? extends MultiPropertyValueMerger> merger() default FirstObjectPropertyValueMerger.class;
    
    /**
     * Extra parameters to send to the value Merger instance.
     * @return a list of parameters that will be passed to the property merger.
     */
    String[] mergerParam() default "";
}
