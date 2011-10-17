package com.juancavallotti.jdto.impl;

import java.lang.annotation.Annotation;
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
 * @author juancavallotti
 */
class BeanPropertyUtils {

    /**
     * Find the fields of a class by inspecting its attributes and getters.
     * @param targetClass
     * @return 
     */
    static HashMap<Field, Method> inspectClassFields(Class targetClass) {
        HashMap<Field, Method> ret = new HashMap<Field, Method>();
        recursiveInspectClass(ret, targetClass);
        return ret;
    }

    static void recursiveInspectClass(HashMap<Field, Method> ret, Class targetClass) {

        //stop when we reach the object class which is not on our interest.
        if (targetClass == Object.class) {
            return;
        }

        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {

            //the field should have a matching getter to be suitable for copying.
            Method getter = findGetterMethod(field, targetClass);

            if (getter == null) {
                continue;
            }

            ret.put(field, getter);
        }

        recursiveInspectClass(ret, targetClass.getSuperclass());
    }

    /**
     * try to find a getter method for a field. Returns null if no getter is 
     * found. <br />
     * 
     * This method tries to find properties on the hard way.
     * @param field
     * @param targetClass
     * @return 
     */
    static Method findGetterMethod(Field field, Class targetClass) {

        String fieldName = StringUtils.capitalize(field.getName());
        Method getter = null;
        try {
            //try to find by appending get to the name
            getter = targetClass.getDeclaredMethod("get" + fieldName);
            return getter;
        } catch (NoSuchMethodException ex) {
            //this we can safely ignore
            //if the get was not found, then try with is, it might be a boolean
            //but first we check if the field is boolean.
            if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                try {
                    getter = targetClass.getDeclaredMethod("is" + fieldName);
                    return getter;
                } catch (NoSuchMethodException exbool) {

                    try {
                        //the final try we should make, is not capitalizing the field
                        //not standard but possible also.
                        getter = targetClass.getDeclaredMethod("is" + field.getName());
                        return getter;
                    } catch (Exception exnoncapital) {
                        return null;
                    }
                }
            } else {
                //the final try we should make, is not capitalizing the field
                //not standard but possible also.
                try {
                    getter = targetClass.getDeclaredMethod("get" + field.getName());
                    return getter;
                } catch (Exception exnoncapital) {
                    return null;
                }
            }
        }
    }

    /**
     * Find an annotation on a field or a getter, giving priority to the field.
     * @param <T>
     * @param annotationClass
     * @param field
     * @param getter
     * @return 
     */
    static <T extends Annotation> T getAnnotation(Class<T> annotationClass, Field field, Method getter) {

        T ret = field.getAnnotation(annotationClass);

        if (ret == null) {
            ret = getter.getAnnotation(annotationClass);
        }

        return ret;
    }

    /**
     * Try to find the actual type value for a property by inspecting the field
     * and the getter method. If the type of the field is Object.class then
     * try to find the type variable on the getter and return it.
     * @param field
     * @param getter
     * @return 
     */
    static Class getGenericType(Field field, Method getter) {


        ParameterizedType fieldParamType = (ParameterizedType) ((field.getGenericType() instanceof ParameterizedType)
                ? field.getGenericType() : null);
        ParameterizedType methodParamType = (ParameterizedType) ((getter.getGenericReturnType() instanceof ParameterizedType)
                ? getter.getGenericReturnType() : null);


        Class ret = Object.class;

        if (fieldParamType != null) {
            ret = (Class) fieldParamType.getActualTypeArguments()[0];
        } else if (methodParamType != null) {
            ret = (Class) methodParamType.getActualTypeArguments()[0];
        }

        return ret;
    }
    
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

            //first of all, we only want public methods.
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            //getters have no arguments.
            if (method.getParameterTypes().length > 0) {
                continue;
            }

            //if the method name doesn't start with get or is, then we don't want it
            if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
                continue;
            }

            //get the method name.
            String methodName = method.getName();

            //convert it to the property name.
            if (methodName.startsWith("get")) {
                methodName = methodName.substring(3);
            } else if (methodName.startsWith("is")) {
                methodName = methodName.substring(2);
            }

            //finaly switch the first uppercase to lowercase
            methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

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

            //first of all, we only want public methods.
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            //setters have just one argument.
            if (method.getParameterTypes().length > 1) {
                continue;
            }

            //if the method name doesn't start with set, then we don't want it
            if (!method.getName().startsWith("set")) {
                continue;
            }

            //get the method name.
            String methodName = method.getName();

            //convert it to the property name.
            if (methodName.startsWith("set")) {
                methodName = methodName.substring(3);
            }

            //finaly switch the first uppercase to lowercase
            methodName = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

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
