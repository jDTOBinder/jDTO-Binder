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

import org.jdto.BeanModifier;
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
     * @return the value read from the property path on the instance or null
     * if something goes wrong.
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
     * @return the value from the property path or null.
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
