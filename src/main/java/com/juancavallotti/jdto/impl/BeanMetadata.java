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
    
}
