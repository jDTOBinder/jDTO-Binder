package com.juancavallotti.jdto;

import java.io.Serializable;

/**
 * Implementations should know how to read or write into beans using 
 * a property path.
 * @author juancavallotti
 */
public interface BeanModifier extends Serializable {
    
    /**
     * Read a value from a property path from an instance of a bean.
     * @param propertyPath
     * @param instance
     * @return 
     */
    public Object readPropertyValue(String propertyPath, Object instance);
    
    /**
     * Write a value to a property following the given path into the given instance.
     * @param propertyPath the property on the instance
     * @param value the value to set.
     * @param instance the instance where to set the value.
     */
    public void writePropertyValue(String propertyPath, Object value, Object instance);
    
}
