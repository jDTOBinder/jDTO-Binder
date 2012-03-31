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
 * way of reading this information by reflection. <br />
 * 
 * The following example demonstrates the use of immutable DTOs:<br />
 * 
 * <!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:auto;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%"><span style="color: #008000; font-weight: bold">public</span> <span style="color: #008000; font-weight: bold">final</span> <span style="color: #008000; font-weight: bold">class</span> <span style="color: #B00060; font-weight: bold">SimpleImmutableDTO</span> <span style="color: #303030">{</span>
 *    <span style="color: #008000; font-weight: bold">private</span> <span style="color: #008000; font-weight: bold">final</span> String firstString<span style="color: #303030">;</span>
 *    <span style="color: #008000; font-weight: bold">private</span> <span style="color: #008000; font-weight: bold">final</span> String secondString<span style="color: #303030">;</span>
 *    
 *    <span style="color: #808080">//make this the DTO constructor.</span>
 *    <span style="color: #505050; font-weight: bold">@DTOConstructor</span>
 *    <span style="color: #008000; font-weight: bold">public</span> <span style="color: #0060B0; font-weight: bold">SimpleImmutableDTO</span><span style="color: #303030">(</span><span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span><span style="background-color: #fff0f0">&quot;myString&quot;</span><span style="color: #303030">)</span> String firstString<span style="color: #303030">,</span> <span style="color: #505050; font-weight: bold">@Source</span><span style="color: #303030">(</span><span style="background-color: #fff0f0">&quot;related.aString&quot;</span><span style="color: #303030">)</span> String secondString<span style="color: #303030">)</span> <span style="color: #303030">{</span>
 *        <span style="color: #008000; font-weight: bold">this</span><span style="color: #303030">.</span><span style="color: #0000C0">firstString</span> <span style="color: #303030">=</span> firstString<span style="color: #303030">;</span>
 *        <span style="color: #008000; font-weight: bold">this</span><span style="color: #303030">.</span><span style="color: #0000C0">secondString</span> <span style="color: #303030">=</span> secondString<span style="color: #303030">;</span>
 *    <span style="color: #303030">}</span>
 *    <span style="color: #303030">...</span> <span style="color: #808080">//GETTERS AND SETTERS</span>
 * <span style="color: #303030">}</span>
 * </pre></div>
 *
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
@Documented
public @interface DTOConstructor {
    
}
