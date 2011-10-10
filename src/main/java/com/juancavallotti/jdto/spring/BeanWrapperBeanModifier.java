package com.juancavallotti.jdto.spring;

import com.juancavallotti.jdto.BeanModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Reads and write property values using the Spring framework {@link BeanWrapper}.
 * @author juancavallotti
 */
public class BeanWrapperBeanModifier implements BeanModifier{
    private static final long serialVersionUID = 1L;
    
    private static Logger logger = LoggerFactory.getLogger(BeanWrapperBeanModifier.class);
    
    /**
     * Read a property value using the property path by invoking a spring {@link BeanWrapper}
     * @param propertyPath
     * @param instance
     * @return 
     */
    public Object readPropertyValue(String propertyPath, Object instance) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(instance);
        
        //check if it's safe to write the property.
        if (!beanWrapper.isReadableProperty(propertyPath)) {
            return null;
        }
        
        return beanWrapper.getPropertyValue(propertyPath);
    }
    
    /**
     * Set a property using the property path invoking a spring framework {@link BeanWrapper}.
     * @param propertyPath
     * @param value
     * @param instance 
     */
    public void writePropertyValue(String propertyPath, Object value, Object instance) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(instance);
        
        if (!beanWrapper.isWritableProperty(propertyPath)) {
            logger.info("Cannot read property path "+propertyPath+" of bean", instance);
            return;
        }
        
        beanWrapper.setPropertyValue(propertyPath, value);
    }
    
}
