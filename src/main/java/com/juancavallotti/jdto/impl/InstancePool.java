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

package com.juancavallotti.jdto.impl;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Pool of instances. This will contain instances of all beans created as singletons.
 * @author juancavallotti
 */
public class InstancePool implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final InstancePool instance = new InstancePool();
    
    private InstancePool() {
        instances = new HashMap<Class, Object>();
    }
    
    private HashMap<Class, Object> instances;
    
    /**
     * Get the current instance of this instance pool.
     * @return 
     */
    public static InstancePool getInstance() {
        return instance;
    }
    
    /**
     * Get or create an instance of an object of type T
     * @param <T> the type of the bean to create.
     * @param cls the class of the bean used as key for this instance pool.
     * @return a new instance or null if the instance could not be created.
     */
    public final synchronized <T> T getOrCreateInstance(Class<T> cls) {
        
        T ret = (T) instances.get(cls);
        
        if (ret == null) {
            ret = BeanClassUtils.createInstance(cls);
            instances.put(cls, ret);
        }
        
        return ret;
    }
    
    /**
     * Get or create an instance of an object of type T
     * @param <T> the type of the bean to create.
     * @param cls the class of the bean used as key for this instance pool.
     * @return a new instance or null if the instance could not be created.
     */
    public static <T> T getOrCreate(Class<T> cls) {
        return getInstance().getOrCreateInstance(cls);
    }
    
}
