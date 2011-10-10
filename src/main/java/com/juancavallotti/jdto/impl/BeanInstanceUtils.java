package com.juancavallotti.jdto.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating instances of classes by reflection. The methods
 * on this class handle reflective creation exceptions and log them properly.
 * @author juancavallotti
 */
class BeanInstanceUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(BeanInstanceUtils.class);
    
    /**
     * Create a new instance of a class or log the exception if the class is
     * not instanceable. This method will return null on the last case.
     * @param <T>
     * @param cls
     * @return 
     */
    static <T> T createInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Throwable t) {
            logger.error("Could not create bean instance of class " + cls.toString(), t);
            throw new RuntimeException(t);
        }
    }
}
