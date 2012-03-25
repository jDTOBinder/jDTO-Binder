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

package org.jdto.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for extracting properties by matching fields and getter methods. <br /> 
 * THIS IS NOT PART OF JDTO PUBLIC API AND REGULAR USERS SHOULD NOT RELY ON THIS CLASS. <br />
 * IT IS MADE PUBLIC JUST FOR PACKAGE ORGANISATION REASONS
 * @author Juan Alberto Lopez Cavallotti
 */
class BeanPropertyUtils {

    /**
     * Get the type parameter out of a generic type.
     * @param genericType
     * @return the type parameter of Object.class
     */
    static Class getGenericTypeParameter(Type genericType) {
        ParameterizedType fieldParamType = null;
        
        if (genericType instanceof ParameterizedType) {
            fieldParamType = (ParameterizedType) genericType;
        }
        
        if (fieldParamType == null) {
            return Object.class;
        }
        
        Class typeParameter = (Class) fieldParamType.getActualTypeArguments()[0];
        
        return typeParameter;
    }
    
    /**
     * Try to find a getter method for the given property on the target class
     * or any of it's parents excluding the object class. This method will
     * try to traverse recursively all of the methods. This may not be the most
     * efficient way to find a getter method, but is included to reduce the
     * amount of dependencies this framework has with third party libraries.
     * @param property
     * @param targetClass
     * @return the expected method or null.
     */
    static Method findGetterMethod(String property, Class targetClass) {

        //we're not accepting the object class
        if (targetClass == Object.class) {
            return null;
        }

        //this is the method list
        Method[] methods = targetClass.getDeclaredMethods();

        for (Method method : methods) {
            
            if (!isGetterMethod(method)) {
                continue;
            }
            
            //get the method name.
            String methodName = convertToPropertyName(method);

            //compare it with the property
            if (property.equals(methodName)) {
                //we got what we wanted.
                return method;
            }
        }

        return findGetterMethod(property, targetClass.getSuperclass());
    }

    /**
     * Find a setter method out of a property name.
     * @param property
     * @param targetClass
     * @return 
     */
    static Method findSetterMethod(String property, Class targetClass) {
        //we're not accepting the object class
        if (targetClass == Object.class) {
            return null;
        }

        //this is the method list
        Method[] methods = targetClass.getDeclaredMethods();

        for (Method method : methods) {

            if (!isSetterMethod(method)) {
                continue;
            }

            //get the method name.
            String methodName = convertToPropertyName(method);

            //compare it with the property
            if (property.equals(methodName)) {
                //we got what we wanted.
                return method;
            }
        }

        return findSetterMethod(property, targetClass.getSuperclass());
    }

    /**
     * Makes discovery of all of the class methods by its getters. <br />
     * This method will recurse into the inheritance hierarchy and will stop
     * on the Object class, finding appropiate getter methods.
     * @param cls the class to ve traversed
     * @return 
     */
    static HashMap<String, Method> discoverPropertiesByGetters(Class cls) {

        HashMap<String, Method> methods = new HashMap<String, Method>();

        //declare a buffer
        Class currentClass = cls;


        //loop to find 
        while (currentClass != Object.class) {

            for (Method method : currentClass.getDeclaredMethods()) {
                if (isGetterMethod(method)) {
                    String propertyName = convertToPropertyName(method);
                    methods.put(propertyName, method);
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return methods;

    }

    static boolean isSetterMethod(Method method) {
        return isAccessorMethod(method, 1, false, "set");
    }

    /**
     * Check if a method is an getter accessor or not. The checks are not 
     * comprehensive, for example any non-boolean method could start with "is"
     * and be taken in account, but there should be no problem with that.
     * @param method
     * @return 
     */
    static boolean isGetterMethod(Method method) {
        return isAccessorMethod(method, 0, true, "get", "is");
    }

    /**
     * Determine wether a method is accessor or not, by looking at some properties.
     * @param method
     * @param maxParams
     * @param hasReturnType 
     * @param prefixes
     * @return 
     */
    private static boolean isAccessorMethod(Method method, int maxParams, boolean hasReturnType, String... prefixes) {

        //first of all, we only want public methods.
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }

        //check if the method should return something or not.
        if (hasReturnType) {
            if (method.getReturnType() == Void.TYPE) {
                return false;
            }
        }

        //check the method parameter length
        int parameterAmount = method.getParameterTypes().length;

        if (parameterAmount > maxParams) {
            return false;
        }

        //check the naming conventions
        String methodName = method.getName();
        if (!StringUtils.startsWithAny(methodName, prefixes)) {
            return false;
        }

        //well everything has succeeded, then is an accessor!

        return true;
    }

    /**
     * Remove the accessor prefix from name and the bean capitalization.
     * This can be used by getters and setters.
     * @param method
     * @return 
     */
    private static String convertToPropertyName(Method method) {
        String methodName = method.getName();

        if (StringUtils.startsWith(methodName, "set")) {
            methodName = StringUtils.removeStart(methodName, "set");
        } else if (StringUtils.startsWith(methodName, "is")) {
            methodName = StringUtils.removeStart(methodName, "is");
        } else if (StringUtils.startsWith(methodName, "get")) {
            methodName = StringUtils.removeStart(methodName, "get");
        }

        //remove the first capital
        return StringUtils.uncapitalize(methodName);
    }
    
    /**
     * Read safely a field from a class. This Method tries to find a field by
     * it's name and silently return null if it is not present. <br />
     * Sadly there is not beautiful way of doing this, but we can safely ignore
     * annotations if no field is found, blame the creators of the reflection
     * API for this.
     * @param sourceClass
     * @param fieldName
     * @return 
     */
    static Field readSafeField(Class sourceClass, String fieldName) {
        try {
            return sourceClass.getDeclaredField(fieldName);
        } catch (Exception ex) {
            return null;
        }
    }
}
