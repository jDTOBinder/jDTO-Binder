package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Merge a {@link Date} or {@link Calendar} using a {@link SimpleDateFormat} 
 * format String. <br />
 * @author juancavallotti
 */
public class DateFormatMerger implements SinglePropertyValueMerger<String, Object> {
    
    /**
     * Format a date using a pattern (provided by the user as extraParam) 
     * applied to a {@link Date} or {@link Calendar} instance.
     * @param value
     * @param extraParam
     * @return 
     */
    public String mergeObjects(Object value, String extraParam) {
        if (!(value instanceof Calendar) && !(value instanceof Date)) {
            throw new IllegalArgumentException("source value is not a Date or Calendar instance!!");
            
        }
        
        //create a dateformat with the format String
        SimpleDateFormat format = new SimpleDateFormat(extraParam);
        
        Date target = null;
        
        if (value instanceof Calendar) {
            target = ((Calendar) value).getTime();
        } else {
            target = (Date) value;
        }
        
        
        return format.format(target);
    }
    
}
