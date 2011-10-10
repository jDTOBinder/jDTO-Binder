package com.juancavallotti.jdto.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for extracting properties by matching fields and getter methods.
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
}
