package org.jdto.mergers;

import org.jdto.SinglePropertyValueMerger;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Merge the value of a Date or Calendar source object by calculating its age
 * in years. <br />
 * 
 * If the input value is not a {@link Date} or {@link Calendar} instance, then
 * throw {@link IllegalArgumentException}. If the input value is null, then
 * return 0.
 * @author Juan Alberto Lopez Cavallotti
 */
public class AgeMerger implements SinglePropertyValueMerger<Double, Object> {
    
    /**
     * 365.25 Days to compensate for leap years.
     */
    private static final long YEARS_L = 31557600000L;
    private static final long WEEKS_L = 604800000L;
    private static final long DAYS_L = 86400000L;
    
    /**
     * A year in milliseconds. This doesn't take really in account leap years
     * but it contains a factor to compensate them every 4 years.
     */
    public static final String YEARS = Long.toString(YEARS_L);
    /**
     * A week in milliseconds.
     */
    public static final String WEEKS = Long.toString(WEEKS_L);
    /**
     * A day in milliseconds.
     */
    public static final String DAYS = Long.toString(DAYS_L);
    
    
    /**
     * Merge the value of a Date or Calendar source object by calculating its age
     * in some time unit. <br />
     * 
     * If the input value is not a {@link Date} or {@link Calendar} instance, then
     * throw {@link IllegalArgumentException}. If the input value is null, then
     * return 0.
     * 
     * @param value
     * @param extraParam a constant to resolve the time unit. If null, then it
     * resolves to years. This constant is expressed in milliseconds and it will
     * be used to calculate the result as (today - value) / constant.
     * @return the age of the param until today.
     */
    @Override
    public Double mergeObjects(Object value, String[] extraParam) {
        
        if (value == null) {
            return 0.0;
        }
        
        if (!(value instanceof Date) && !(value instanceof Calendar)) {
            throw new IllegalArgumentException("Value is neither a Date or a Calendar instance.");
        }
        
        
        //convert the value to a calendar.
        double todayStamp = Calendar.getInstance().getTimeInMillis();
        
        double otherStamp = getOtherStamp(value);
        
        double constant = resolveConstant(extraParam);
        
        return (todayStamp - otherStamp) / constant;
        
    }

    private long getOtherStamp(Object value) {
        if (value instanceof Date) {
            Date ret = (Date) value;
            return ret.getTime();
        }
        
        if (value instanceof Calendar) {
            Calendar ret = (Calendar) value;
            return ret.getTimeInMillis();
        }
        return 0;
    }

    private long resolveConstant(String[] extraParams) {
        if (ArrayUtils.isEmpty(extraParams)) {
            return YEARS_L;
        }
        if (StringUtils.equals(extraParams[0], YEARS)) {
            return YEARS_L;
        }
        
        if (StringUtils.equals(extraParams[0], WEEKS)) {
            return WEEKS_L;
        }
        
        if (StringUtils.equals(extraParams[0], DAYS)) {
            return DAYS_L;
        }
        
        return Long.parseLong(extraParams[0]);
    }
}
