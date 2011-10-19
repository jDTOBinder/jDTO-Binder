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

package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.BeanModifier;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of bean modifier, basicly to let the user to not depend
 * on unwanted third party libraries. Please see documentation for information
 * on what third party libraries are really required to make this framework work.
 * @author juancavallotti
 */
public class CoreBeanModifier implements BeanModifier {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CoreBeanModifier.class);
    private boolean makeUpMissingInstances;
    private HashMap<Class, HashMap<String, Method>> getterCache;
    private HashMap<Class, HashMap<String, Method>> setterCache;

    /**
     * Create a new instance of the core bean modifer. By default this 
     * modifier makes up the missing instances on the property path, only when
     * trying to write some value.
     */
    public CoreBeanModifier() {
        this.makeUpMissingInstances = true;
        getterCache = new HashMap<Class, HashMap<String, Method>>();
        setterCache = new HashMap<Class, HashMap<String, Method>>();
    }

    /**
     * Create a new instance of the core bean modifier with the make up setting
     * as a parameter. This setting will decide wether to make up for missing
     * objects while trying to set some property.
     * @param makeUpMissingInstances 
     */
    public CoreBeanModifier(boolean makeUpMissingInstances) {
        this();
        this.makeUpMissingInstances = makeUpMissingInstances;
    }

    /**
     * {@inheritDoc } 
     */
    @Override
    public Object readPropertyValue(String propertyPath, Object instance) {

        try {

            if (propertyPath == null) {
                logger.info("Ignoring null property path");
                return null;
            }

            if (instance == null) {
                logger.info("Ignoring attempt to read a value from a null instance");
                return null;
            }

            //first try to split the property path by its parts.
            String[] partialProperties = StringUtils.split(propertyPath, ".");

            Object actualInstance = instance;

            for (String property : partialProperties) {

                Method getter = findProperGetter(property, actualInstance.getClass());

                if (getter == null) {
                    logger.warn("no getter method found for property " + property + " on class " + actualInstance.getClass().getName());
                    return null;
                }

                Object value = getter.invoke(actualInstance);

                if (value == null) {
                    return null;
                }

                actualInstance = value;
            }
            //end of recursion, we can now return the value we found.
            return actualInstance;
        } catch (Exception ex) {
            logger.error("Got unmanaged exception while trying to read property path " + propertyPath, ex);
            return null;
        }
    }

    /**
     * {@inheritDoc }
     * @param propertyPath
     * @param finalValue
     * @param instance 
     */
    @Override
    public void writePropertyValue(String propertyPath, Object finalValue, Object instance) {
        try {

            if (propertyPath == null) {
                logger.info("Ignoring null property path");
                return;
            }

            if (instance == null) {
                logger.info("Ignoring attempt to write a value to a null instance (I can't make up for this)");
                return;
            }

            //first try to split the property path by its parts.
            String[] partialProperties = StringUtils.split(propertyPath, ".");

            Object actualInstance = instance;

            for (int i = 0; i < partialProperties.length - 1; i++) {

                String property = partialProperties[i];
                //here comes the make up magic.
                //first of all we find the getter.
                Method getter = findProperGetter(property, actualInstance.getClass());

                if (getter == null) {
                    logger.warn("no getter method found for property " + property + " on class " + actualInstance.getClass().getName());
                    return;
                }

                Object value = getter.invoke(actualInstance);

                //make up magic
                if (value == null) {

                    //if is not the last on the property pathn then we need to make up
                    if (makeUpMissingInstances) {
                        Method setter = findProperSetter(property, actualInstance.getClass());
                        //try to create a new made up value.
                        //this may not always be possible.
                        Object madeUpValue = BeanClassUtils.createInstance(getter.getReturnType());
                        if (madeUpValue == null) {
                            logger.info("Found null on property path and couldn't make up the new value");
                            return;
                        }
                        actualInstance = madeUpValue;
                    } else {
                        logger.info("Found null on property path and I'm not configured to create new instances");
                        return;
                    }
                    return;
                }

                actualInstance = value;
            }
            //end of recursion, we can now return the value we found.

            //finally the strawberry on the dessert.
            Method setter = findProperSetter(partialProperties[partialProperties.length - 1], actualInstance.getClass());

            //at this point maybe a little type casting it would be helpful
            Class expectedType = setter.getParameterTypes()[0];

            //try to cast
            if (expectedType.isArray()) {
                finalValue = copyToProperArray(expectedType, finalValue);
            }

            //set it
            setter.invoke(actualInstance, finalValue);
        } catch (Exception ex) {
            logger.error("Got unmanaged exception while trying to write on property path " + propertyPath, ex);
            return;
        }
    }

    //GETTERS AND SETTERS
    public boolean isMakeUpMissingInstances() {
        return makeUpMissingInstances;
    }

    public void setMakeUpMissingInstances(boolean makeUpMissingInstances) {
        this.makeUpMissingInstances = makeUpMissingInstances;
    }

    private synchronized Method findProperGetter(String property, Class targetClass) {

        HashMap<String, Method> classGetterCache = getterCache.get(targetClass);

        if (classGetterCache == null) {
            classGetterCache = new HashMap();
            getterCache.put(targetClass, classGetterCache);
        }

        Method ret = classGetterCache.get(property);

        if (ret != null) {
            return ret;
        }

        ret = BeanPropertyUtils.findGetterMethod(property, targetClass);

        classGetterCache.put(property, ret);

        return ret;
    }

    private synchronized Method findProperSetter(String property, Class targetClass) {

        HashMap<String, Method> classSetterCache = setterCache.get(targetClass);

        if (classSetterCache == null) {
            classSetterCache = new HashMap();
            setterCache.put(targetClass, classSetterCache);
        }


        Method ret = classSetterCache.get(property);

        if (ret != null) {
            return ret;
        }

        ret = BeanPropertyUtils.findSetterMethod(property, targetClass);

        classSetterCache.put(property, ret);

        return ret;
    }

    private Object copyToProperArray(Class expectedType, Object finalValue) {
        Object ret = Array.newInstance(expectedType.getComponentType(), Array.getLength(finalValue));
        
        for (int i = 0; i < Array.getLength(finalValue); i++) {
            Array.set(ret, i, Array.get(finalValue, i));
        }
        
        return ret;
    }
}
