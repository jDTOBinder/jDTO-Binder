package com.juancavallotti.jdto;

import java.util.List;

/**
 * Implementations should know how to merge a list of objects into a single object. <br />
 * This interface is meant to be used to create a single value out of a multi-source
 * property configuration, see {@link com.juancavallotti.jdto.annotation.Sources}.
 * @param <T> The result type of the merged parameters.
 * @author juancavallotti
 */
public interface MultiPropertyValueMerger<T> {
    
    /**
     * Merge the list of objects into a single object.
     * @param values the values to be merged.
     * @param extraParam metadata that may help the merger to build the result.
     * @return 
     */
    public T mergeObjects(List<Object> values, String extraParam);
}
