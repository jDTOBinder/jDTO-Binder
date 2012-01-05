package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.BeanModifier;
import java.io.Serializable;
import java.util.Map;

/**
 * Base class for bean modifiers, this adds basic features common to all 
 * bean modifiers. This class adds features for reading / writing from or to
 * a map transparently.
 * @author juancavallotti
 */
public abstract class BaseBeanModifier implements BeanModifier, Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * {@inheritDoc }
     * If the instance parameter is a map, then it will read the value from it
     * using the propertyPath as the key.
     * @param propertyPath
     * @param instance
     * @return 
     */
    @Override
    public final Object readPropertyValue(String propertyPath, Object instance) {
        
        if (instance instanceof Map) {
            return readValueFromMap(propertyPath, (Map) instance);
        }
        
        return doReadPropertyValue(propertyPath, instance);
    }
    
    /**
     * {@inheritDoc }
     * If the instance parameter is a map, then it will write the value into it
     * using the property path as the key.
     * @param propertyPath
     * @param value
     * @param instance 
     */
    @Override
    public final void writePropertyValue(String propertyPath, Object value, Object instance) {
        
        if (instance instanceof Map) {
            writeValueToMap(propertyPath, value, (Map) instance);
            return;
        }
        
        doWritePropertyValue(propertyPath, value, instance);
    }
    
    /**
     * Read a property value.
     * @param propertyPath
     * @param instace
     * @return 
     */
    protected abstract Object doReadPropertyValue(String propertyPath, Object instace);
    
    /**
     * Write a property value.
     * @param propertyPath
     * @param value
     * @param instance 
     */
    protected abstract void doWritePropertyValue(String propertyPath, Object value, Object instance);

    private Object readValueFromMap(String propertyPath, Map instance) {
        return instance.get(propertyPath);
    }

    private void writeValueToMap(String propertyPath, Object value, Map instance) {
        instance.put(propertyPath, value);
    }
}
