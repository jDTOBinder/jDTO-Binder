package com.juancavallotti.jdto.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating instances of classes by reflection. The methods
 * on this class handle reflective creation exceptions and log them properly.
 * @author juancavallotti
 */
class BeanClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(BeanClassUtils.class);

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

    /**
     * Try to find a class out of a string or return null;
     * @param type
     * @return 
     */
    static Class safeGetClass(String type) {
        //if no type then no class :D
        if (StringUtils.isEmpty(type)) {
            return null;
        }

        try {
            return Class.forName(type);
        } catch (Exception ex) {
            logger.error("Error while trying to read the dto class", ex);
            return null;
        }
    }
}
