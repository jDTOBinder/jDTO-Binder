package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.util.Arrays;
import java.util.List;

/**
 * This merger takes the formatting string (see {@link String#format(java.lang.String, java.lang.Object[]) })
 * as the merger extra parameter and returns a String. <br />
 * For more information see {@link com.juancavallotti.jdto.annotation.Source#merger() } and
 * {@link com.juancavallotti.jdto.annotation.Sources#merger() } documentation.
 * @author juancavallotti
 */
public class StringFormatMerger implements MultiPropertyValueMerger<String>, SinglePropertyValueMerger<String, Object> {
    
    /**
     * Merges the objects using the extraParam as a formatting String.
     * @param values
     * @param extraParam
     * @return 
     */
    public String mergeObjects(List<Object> values, String extraParam) {
        Object[] params = values.toArray();
        return String.format(extraParam, params);
    }
    
    /**
     * Delegate to the mergeObjects method.
     * @param value
     * @param extraParam
     * @return 
     */
    public String mergeObjects(Object value, String extraParam) {
        return mergeObjects(Arrays.asList(value), extraParam);
    }
    
}
