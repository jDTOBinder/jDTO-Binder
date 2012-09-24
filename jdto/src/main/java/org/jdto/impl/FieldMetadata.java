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
import java.util.HashMap;
import java.util.List;
import org.jdto.MultiPropertyValueMerger;
import org.jdto.SinglePropertyValueMerger;

/**
 * Represents the metadata for a given field.
 * @author Juan Alberto Lopez Cavallotti
 */
public class FieldMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<String> sourceFields;
    private Class[] sourceMergers;
    private String[][] sourceMergersParams;
    private String[] sourceBeans;
    private String[] mergerParameters;
    private Class<? extends MultiPropertyValueMerger> propertyValueMerger;
    private String[] sourceBeanNames;
    private Class targetType;
    
    /**
     * convenience attribute.
     */
    private boolean cascadePresent;
    
    private CascadeType cascadeType;
    
    private Class cascadeTargetClass;
    
    private boolean fieldTransient;
    
    public FieldMetadata() {
        sourceMergers = null;
        sourceMergersParams = null;
        sourceBeans = null;
    }
    
    void initSourceFieldWithSize(int amount) {
        sourceMergers = new Class[amount];
        sourceMergersParams = new String[amount][];
        sourceBeans = new String[amount];
    }
    
    public String[] getMergerParameter() {
        return mergerParameters;
    }

    public void setMergerParameter(String[] mergerParameter) {
        this.mergerParameters = mergerParameter;
    }

    public Class<? extends MultiPropertyValueMerger> getPropertyValueMerger() {
        return propertyValueMerger;
    }

    public void setPropertyValueMerger(Class propertyValueMerger) {
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

    public Class[] getSourceMergers() {
        return sourceMergers;
    }

    public void setSourceMergers(Class[] sourceMergers) {
        this.sourceMergers = sourceMergers;
    }

    public String[][] getSourceMergersParams() {
        return sourceMergersParams;
    }

    public void setSourceMergersParams(String[][] sourceMergersParams) {
        this.sourceMergersParams = sourceMergersParams;
    }

    public String[] getSourceBeanNames() {
        return sourceBeanNames;
    }

    public void setSourceBeanNames(String[] sourceBeanNames) {
        this.sourceBeanNames = sourceBeanNames;
    }

    public String[] getSourceBeans() {
        return sourceBeans;
    }

    public void setSourceBeans(String[] sourceBeans) {
        this.sourceBeans = sourceBeans;
    }
    
    public void setSinglePropertyValueMerger(String propertyName, Class<SinglePropertyValueMerger> merger, String[] extraParams, String sourceBean, int sourcePropertyIndex) {
        sourceMergers[sourcePropertyIndex] = merger;
        sourceMergersParams[sourcePropertyIndex] = extraParams;
        sourceBeans[sourcePropertyIndex] = sourceBean;
    }
    
    /**
     * The type of the field
     * @return 
     */
    public Class getTargetType() {
        return targetType;
    }

    public void setTargetType(Class targetType) {
        this.targetType = targetType;
    }
    
}
