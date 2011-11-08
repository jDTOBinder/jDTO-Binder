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

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Represents useful metadata needed to bind a bean.
 * @author juancavallotti
 */
public class BeanMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String[] defaultBeanNames;
    private HashMap<String, FieldMetadata> fieldMetadata;
    
    public BeanMetadata() {
        fieldMetadata = new HashMap<String, FieldMetadata>();
    }
    
    
    void putFieldMetadata(String beanProperty, List<String> sourceProperties, MultiPropertyValueMerger merger) {
        FieldMetadata metadata = getMetadata(beanProperty);
        metadata.setSourceFields(sourceProperties);
        metadata.setPropertyValueMerger(merger);
    }
    
    void putFieldMetadata(String beanProperty, FieldMetadata metadata) {
        fieldMetadata.put(beanProperty, metadata);
    }
    
    //GETTERS AND SETTERS

    public HashMap<String, FieldMetadata> getFieldMetadata() {
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

    public String[] getDefaultBeanNames() {
        return defaultBeanNames;
    }

    public void setDefaultBeanNames(String[] defaultBeanNames) {
        this.defaultBeanNames = defaultBeanNames;
    }
    
}
