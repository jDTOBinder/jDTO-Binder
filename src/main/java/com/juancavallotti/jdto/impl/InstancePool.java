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
