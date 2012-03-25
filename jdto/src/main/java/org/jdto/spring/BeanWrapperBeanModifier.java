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

package org.jdto.spring;

import org.jdto.impl.BaseBeanModifier;
import org.jdto.impl.BeanClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Reads and write property values using the Spring framework {@link BeanWrapper}. <br />
 * When trying to write, this bean will try if possible to make up for missing
 * instances on the association path.
 * @author Juan Alberto Lopez Cavallotti
 */
public class BeanWrapperBeanModifier extends BaseBeanModifier{
    private static final long serialVersionUID = 1L;
    
    private static Logger logger = LoggerFactory.getLogger(BeanWrapperBeanModifier.class);
    
    /**
     * Read a property value using the property path by invoking a spring {@link BeanWrapper}
     * @param propertyPath
     * @param instance
     * @return the property value found on the property path applied to the provided instance.
     */
    @Override
    public Object doReadPropertyValue(String propertyPath, Object instance) {
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
    @Override
    public void doWritePropertyValue(String propertyPath, Object value, Object instance) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(instance);
        
        
        //check and make up for missing association parts.
        StringBuilder builder = new StringBuilder();
        
        String[] subProps = StringUtils.split(propertyPath, '.');
        
        //go through all the parts but one
        for (int i = 0; i < subProps.length - 1; i++) {
            String prop = subProps[i];
            
            if (i > 0) {
                builder.append(".");
            }
            
            builder.append(prop);
            
            Object partialValue = beanWrapper.getPropertyValue(builder.toString());
            if (partialValue == null) {
                //make up for it
                Class propCls = beanWrapper.getPropertyType(builder.toString());
                Object madeUpValue = BeanClassUtils.createInstance(propCls);
                if (madeUpValue != null) {
                    if (beanWrapper.isWritableProperty(builder.toString())) {
                        beanWrapper.setPropertyValue(builder.toString(), madeUpValue);
                    }
                }
            }
        }
        
        
        if (!beanWrapper.isWritableProperty(propertyPath)) {
            logger.info("Cannot write property path "+propertyPath+" of bean", instance);
            return;
        }
        
        beanWrapper.setPropertyValue(propertyPath, value);
    }
    
}
