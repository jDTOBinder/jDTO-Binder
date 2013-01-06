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

package org.jdto.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.jdto.DTOMetadata;
import org.jdto.MultiPropertyValueMerger;

/**
 * Represents useful metadata needed to bind a bean.
 * @author Juan Alberto Lopez Cavallotti
 */
public class BeanMetadata implements DTOMetadata,Serializable {
    private static final long serialVersionUID = 1L;
    
    private String[] defaultBeanNames;
    private HashMap<String, FieldMetadata> fieldMetadata;
    private boolean immutableBean;
    private List<FieldMetadata> constructorArgs;
    private Constructor immutableConstructor;
    
    private EnumMap<LifecyclePhase, Method> lifecycleHandlers;
    
    /**
     * Initialize the metadata object for a standard bean.
     */
    public BeanMetadata() {
        this(false);
    }
    
    
    /**
     * Initialize the metadata object. If the argument is true, then make the
     * initialization optimized for immutable beans, otherwise, make the normal
     * initalization.
     * @param immutableBean 
     */
    public BeanMetadata(boolean immutableBean) {
        lifecycleHandlers = new EnumMap<LifecyclePhase, Method>(LifecyclePhase.class);
        if (immutableBean) {
            this.immutableBean = immutableBean;
            constructorArgs = new LinkedList<FieldMetadata>();
        } else {
            fieldMetadata = new HashMap<String, FieldMetadata>();
        }
    }
    
    void putFieldMetadata(String beanProperty, List<String> sourceProperties, Class<MultiPropertyValueMerger> merger) {
        FieldMetadata metadata = getMetadata(beanProperty);
        metadata.setSourceFields(sourceProperties);
        metadata.setPropertyValueMerger(merger);
    }
    
    void putFieldMetadata(String beanProperty, FieldMetadata metadata) {
        fieldMetadata.put(beanProperty, metadata);
    }
    
    /**
     * Add an extra field metadata matched with a constructor argument.
     * @param metadata 
     */
    void addConstructorArgMetadata(FieldMetadata metadata) {
        constructorArgs.add(metadata);
    }
    
    //GETTERS AND SETTERS

    @Override
    public HashMap getFieldMetadata() {
        return fieldMetadata;
    }

    public void setFieldMetadata(HashMap<String, FieldMetadata> fieldMetadata) {
        this.fieldMetadata = fieldMetadata;
    }
    
    private FieldMetadata getMetadata(String beanProperty) {
        FieldMetadata ret = fieldMetadata.get(beanProperty);
        if (ret == null) {
            ret = new FieldMetadata();
            fieldMetadata.put(beanProperty, ret);
        }
        return ret;
    }

    @Override
    public String[] getDefaultBeanNames() {
        return defaultBeanNames;
    }

    public void setDefaultBeanNames(String[] defaultBeanNames) {
        this.defaultBeanNames = defaultBeanNames;
    }

    @Override
    public List getConstructorArgs() {
        return constructorArgs;
    }

    @Override
    public boolean isImmutableBean() {
        return immutableBean;
    }

    @Override
    public Constructor getImmutableConstructor() {
        return immutableConstructor;
    }

    public void setImmutableConstructor(Constructor immutableConstructor) {
        this.immutableConstructor = immutableConstructor;
    }

    EnumMap<LifecyclePhase, Method> getLifecycleHandlers() {
        return lifecycleHandlers;
    }

    void setLifecycleHandlers(EnumMap<LifecyclePhase, Method> lifecycleHandlers) {
        this.lifecycleHandlers = lifecycleHandlers;
    }
    
}
