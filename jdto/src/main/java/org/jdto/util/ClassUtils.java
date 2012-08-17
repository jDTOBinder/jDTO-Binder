/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdto.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;

/**
 * This class has been extracted from the ClassUtils provided on the Apache
 * Commons-Lang library, the methods present in this class were copied here so
 * the dependency with that library could be relaxed to a lower version.
 * 
 * Please note that this class may be removed on the future.
 *
 * @author Juan Alberto Lopez Cavallotti
 */
public class ClassUtils {

    /**
     * Maps primitive
     * <code>Class</code>es to their corresponding wrapper
     * <code>Class</code>.
     */
    private static final Map primitiveWrapperMap = new HashMap();

    static {
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }
    /**
     * Maps wrapper
     * <code>Class</code>es to their corresponding primitive types.
     */
    private static final Map wrapperPrimitiveMap = new HashMap();

    static {
        for (Iterator it = primitiveWrapperMap.keySet().iterator(); it.hasNext();) {
            Class primitiveClass = (Class) it.next();
            Class wrapperClass = (Class) primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
    }
    // Is assignable
    // ----------------------------------------------------------------------

    /**
     * <p>Checks if an array of Classes can be assigned to another array of
     * Classes.</p>
     *
     * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for
     * each Class pair in the input arrays. It can be used to check if a set of
     * arguments (the first parameter) are suitably compatible with a set of
     * method parameter types (the second parameter).</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a
     * <code>long</code>,
     * <code>float</code> or
     * <code>double</code>. This method returns the correct result for these
     * cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return
     * <code>true</code> if
     * <code>null</code> is passed in and the toClass is non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified
     * <code>Class</code> parameter can be converted to the type represented by
     * this
     * <code>Class</code> object via an identity conversion widening primitive
     * or widening reference conversion. See <em><a
     * href="http://java.sun.com/docs/books/jls/">The Java Language
     * Specification</a></em>, sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param classArray the array of Classes to check, may be
     * <code>null</code>
     * @param toClassArray the array of Classes to try to assign into, may be
     * <code>null</code>
     * @return
     * <code>true</code> if assignment possible
     */
    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        return isAssignable(classArray, toClassArray, false);
    }

    /**
     * <p>Checks if an array of Classes can be assigned to another array of
     * Classes.</p>
     *
     * <p>This method calls {@link #isAssignable(Class, Class) isAssignable} for
     * each Class pair in the input arrays. It can be used to check if a set of
     * arguments (the first parameter) are suitably compatible with a set of
     * method parameter types (the second parameter).</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a
     * <code>long</code>,
     * <code>float</code> or
     * <code>double</code>. This method returns the correct result for these
     * cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return
     * <code>true</code> if
     * <code>null</code> is passed in and the toClass is non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified
     * <code>Class</code> parameter can be converted to the type represented by
     * this
     * <code>Class</code> object via an identity conversion widening primitive
     * or widening reference conversion. See <em><a
     * href="http://java.sun.com/docs/books/jls/">The Java Language
     * Specification</a></em>, sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param classArray the array of Classes to check, may be
     * <code>null</code>
     * @param toClassArray the array of Classes to try to assign into, may be
     * <code>null</code>
     * @param autoboxing whether to use implicit autoboxing/unboxing between
     * primitives and wrappers
     * @return
     * <code>true</code> if assignment possible
     * @since 2.5
     */
    public static boolean isAssignable(Class[] classArray, Class[] toClassArray, boolean autoboxing) {
        if (ArrayUtils.isSameLength(classArray, toClassArray) == false) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (isAssignable(classArray[i], toClassArray[i], autoboxing) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if one
     * <code>Class</code> can be assigned to a variable of another
     * <code>Class</code>.</p>
     *
     * <p>Unlike the {@link Class#isAssignableFrom(java.lang.Class)} method,
     * this method takes into account widenings of primitive classes and
     * <code>null</code>s.</p>
     *
     * <p>Primitive widenings allow an int to be assigned to a long, float or
     * double. This method returns the correct result for these cases.</p>
     *
     * <p><code>Null</code> may be assigned to any reference type. This method
     * will return
     * <code>true</code> if
     * <code>null</code> is passed in and the toClass is non-primitive.</p>
     *
     * <p>Specifically, this method tests whether the type represented by the
     * specified
     * <code>Class</code> parameter can be converted to the type represented by
     * this
     * <code>Class</code> object via an identity conversion widening primitive
     * or widening reference conversion. See <em><a
     * href="http://java.sun.com/docs/books/jls/">The Java Language
     * Specification</a></em>, sections 5.1.1, 5.1.2 and 5.1.4 for details.</p>
     *
     * @param cls the Class to check, may be null
     * @param toClass the Class to try to assign into, returns false if null
     * @param autoboxing whether to use implicit autoboxing/unboxing between
     * primitives and wrappers
     * @return
     * <code>true</code> if assignment possible
     * @since 2.5
     */
    public static boolean isAssignable(Class cls, Class toClass, boolean autoboxing) {
        if (toClass == null) {
            return false;
        }
        // have to check for null, as isAssignableFrom doesn't
        if (cls == null) {
            return !(toClass.isPrimitive());
        }
        //autoboxing:
        if (autoboxing) {
            if (cls.isPrimitive() && !toClass.isPrimitive()) {
                cls = primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                }
            }
            if (toClass.isPrimitive() && !cls.isPrimitive()) {
                cls = wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                }
            }
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (cls.isPrimitive()) {
            if (toClass.isPrimitive() == false) {
                return false;
            }
            if (Integer.TYPE.equals(cls)) {
                return Long.TYPE.equals(toClass)
                        || Float.TYPE.equals(toClass)
                        || Double.TYPE.equals(toClass);
            }
            if (Long.TYPE.equals(cls)) {
                return Float.TYPE.equals(toClass)
                        || Double.TYPE.equals(toClass);
            }
            if (Boolean.TYPE.equals(cls)) {
                return false;
            }
            if (Double.TYPE.equals(cls)) {
                return false;
            }
            if (Float.TYPE.equals(cls)) {
                return Double.TYPE.equals(toClass);
            }
            if (Character.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                        || Long.TYPE.equals(toClass)
                        || Float.TYPE.equals(toClass)
                        || Double.TYPE.equals(toClass);
            }
            if (Short.TYPE.equals(cls)) {
                return Integer.TYPE.equals(toClass)
                        || Long.TYPE.equals(toClass)
                        || Float.TYPE.equals(toClass)
                        || Double.TYPE.equals(toClass);
            }
            if (Byte.TYPE.equals(cls)) {
                return Short.TYPE.equals(toClass)
                        || Integer.TYPE.equals(toClass)
                        || Long.TYPE.equals(toClass)
                        || Float.TYPE.equals(toClass)
                        || Double.TYPE.equals(toClass);
            }
            // should never get here
            return false;
        }
        return toClass.isAssignableFrom(cls);
    }

    /**
     * <p>Converts the specified primitive Class object to its corresponding
     * wrapper Class object.</p>
     *
     * <p>NOTE: From v2.2, this method handles
     * <code>Void.TYPE</code>, returning
     * <code>Void.TYPE</code>.</p>
     *
     * @param cls the class to convert, may be null
     * @return the wrapper class for
     * <code>cls</code> or
     * <code>cls</code> if
     * <code>cls</code> is not a primitive.
     * <code>null</code> if null input.
     * @since 2.1
     */
    public static Class primitiveToWrapper(Class cls) {
        Class convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = (Class) primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }

    /**
     * <p>Converts the specified wrapper class to its corresponding primitive
     * class.</p>
     *
     * <p>This method is the counter part of
     * <code>primitiveToWrapper()</code>. If the passed in class is a wrapper
     * class for a primitive type, this primitive type will be returned (e.g.
     * <code>Integer.TYPE</code> for
     * <code>Integer.class</code>). For other classes, or if the parameter is
     * <b>null</b>, the return value is <b>null</b>.</p>
     *
     * @param cls the class to convert, may be <b>null</b>
     * @return the corresponding primitive type if
     * <code>cls</code> is a wrapper class, <b>null</b> otherwise
     * @see #primitiveToWrapper(Class)
     * @since 2.4
     */
    public static Class wrapperToPrimitive(Class cls) {
        return (Class) wrapperPrimitiveMap.get(cls);
    }
}
