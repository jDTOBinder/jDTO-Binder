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
