package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.SinglePropertyValueMerger;

/**
 * Returns the same instance it gets as parameter. This is the default property
 * merger, it just keeps the original value
 * as it is. <br />
 * 
 * This behavior is helpful when the developer wants the value to remain the same
 * across the copy process. <br />
 * 
 * @author juancavallotti
 */
public final class IdentityPropertyValueMerger implements SinglePropertyValueMerger<Object, Object> {
    
    /**
     * Return the same value it gets as parameter.
     * @param value the value to be retuned.
     * @param extraParam ignored.
     * @return the same value received.
     */
    public Object mergeObjects(Object value, String extraParam) {
        return value;
    }
    
}
