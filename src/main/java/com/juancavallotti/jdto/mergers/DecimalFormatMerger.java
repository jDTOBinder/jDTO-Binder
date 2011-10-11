package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.text.DecimalFormat;

/**
 * Format a given number using a {@link DecimalFormat} instance with the format
 * string as the extra param. Please refer to the {@link DecimalFormat} documentation
 * to check how to write format Strings.
 * @author juancavallotti
 */
public class DecimalFormatMerger implements SinglePropertyValueMerger<String, Number> {
    
    /**
     * Merge a number by using a {@link DecimalFormat} instance.
     * @param value
     * @param extraParam
     * @return 
     */
    public String mergeObjects(Number value, String extraParam) {
        
        DecimalFormat format = new DecimalFormat(extraParam);
        
        return format.format(value);
    }
    
}
