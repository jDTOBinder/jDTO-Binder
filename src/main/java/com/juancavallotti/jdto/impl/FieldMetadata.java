package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
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
    private HashMap<String, String> sourceMergersParams;
    private HashMap<String, String> sourceBeans;
    private String mergerParameter;
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
        sourceMergersParams = new HashMap<String, String>();
        sourceBeans = new HashMap<String, String>();
    }

    public String getMergerParameter() {
        return mergerParameter;
    }

    public void setMergerParameter(String mergerParameter) {
        this.mergerParameter = mergerParameter;
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

    public HashMap<String, String> getSourceMergersParams() {
        return sourceMergersParams;
    }

    public void setSourceMergersParams(HashMap<String, String> sourceMergersParams) {
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
    
    public void addSinglePropertyValueMerger(String propertyName, SinglePropertyValueMerger merger, String extraParam, String sourceBean) {
        sourceMergers.put(propertyName, merger);
        sourceMergersParams.put(propertyName, extraParam);
        sourceBeans.put(propertyName, sourceBean);
    }
}
