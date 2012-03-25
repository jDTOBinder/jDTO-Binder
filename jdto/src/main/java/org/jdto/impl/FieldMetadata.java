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

import org.jdto.MultiPropertyValueMerger;
import org.jdto.SinglePropertyValueMerger;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Represents the metadata for a given field.
 * @author juancavallotti
 */
public class FieldMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<String> sourceFields;
    private HashMap<String, SinglePropertyValueMerger> sourceMergers;
    private HashMap<String, String[]> sourceMergersParams;
    private HashMap<String, String> sourceBeans;
    private String[] mergerParameters;
    private MultiPropertyValueMerger propertyValueMerger;
    private String[] sourceBeanNames;
    
    /**
     * convenience attribute.
     */
    private boolean cascadePresent;
    
    private CascadeType cascadeType;
    
    private Class cascadeTargetClass;
    
    private boolean fieldTransient;
    
    public FieldMetadata() {
        sourceMergers = new HashMap<String, SinglePropertyValueMerger>();
        sourceMergersParams = new HashMap<String, String[]>();
        sourceBeans = new HashMap<String, String>();
    }

    public String[] getMergerParameter() {
        return mergerParameters;
    }

    public void setMergerParameter(String[] mergerParameter) {
        this.mergerParameters = mergerParameter;
    }

    public MultiPropertyValueMerger getPropertyValueMerger() {
        return propertyValueMerger;
    }

    public void setPropertyValueMerger(MultiPropertyValueMerger propertyValueMerger) {
        this.propertyValueMerger = propertyValueMerger;
    }

    public List<String> getSourceFields() {
        return sourceFields;
    }

    public void setSourceFields(List<String> sourceFields) {
        this.sourceFields = sourceFields;
    }

    public boolean isFieldTransient() {
        return fieldTransient;
    }

    public void setFieldTransient(boolean fieldTransient) {
        this.fieldTransient = fieldTransient;
    }

    public boolean isCascadePresent() {
        return cascadePresent;
    }

    public void setCascadePresent(boolean cascadePresent) {
        this.cascadePresent = cascadePresent;
    }

    public Class getCascadeTargetClass() {
        return cascadeTargetClass;
    }

    public void setCascadeTargetClass(Class cascadeTargetClass) {
        this.cascadeTargetClass = cascadeTargetClass;
    }

    public CascadeType getCascadeType() {
        return cascadeType;
    }

    public void setCascadeType(CascadeType cascadeType) {
        this.cascadeType = cascadeType;
    }

    public HashMap<String, SinglePropertyValueMerger> getSourceMergers() {
        return sourceMergers;
    }

    public void setSourceMergers(HashMap<String, SinglePropertyValueMerger> sourceMergers) {
        this.sourceMergers = sourceMergers;
    }

    public HashMap<String, String[]> getSourceMergersParams() {
        return sourceMergersParams;
    }

    public void setSourceMergersParams(HashMap<String, String[]> sourceMergersParams) {
        this.sourceMergersParams = sourceMergersParams;
    }

    public String[] getSourceBeanNames() {
        return sourceBeanNames;
    }

    public void setSourceBeanNames(String[] sourceBeanNames) {
        this.sourceBeanNames = sourceBeanNames;
    }

    public HashMap<String, String> getSourceBeans() {
        return sourceBeans;
    }

    public void setSourceBeans(HashMap<String, String> sourceBeans) {
        this.sourceBeans = sourceBeans;
    }
    
    public void setSinglePropertyValueMerger(String propertyName, SinglePropertyValueMerger merger, String[] extraParams, String sourceBean) {
        sourceMergers.put(propertyName, merger);
        sourceMergersParams.put(propertyName, extraParams);
        sourceBeans.put(propertyName, sourceBean);
    }
}
