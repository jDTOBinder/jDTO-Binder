/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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

package org.jdto.mergers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdto.SinglePropertyValueMerger;

import java.util.Calendar;
import java.util.Date;

/**
 * Merge the value of a Date or Calendar source object by calculating its age
 * in years. <br />
 * 
 * If the input value is not a {@link Date} or {@link Calendar} instance, then
 * throw {@link IllegalArgumentException}. If the input value is null, then
 * return 0.
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class AgeMerger implements SinglePropertyValueMerger<Double, Object> {
    
    private static final long serialVersionUID = 1L;
    
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
                
        return (double) Math.round((todayStamp - otherStamp) / constant);
        
    }

    @Override
    public boolean isRestoreSupported(String[] params) {
        //there is no way yo know the original date by just an age.
        return false;
    }

    @Override
    public Object restoreObject(Double object, String[] params) {
        return null; //to fulfill the contract.
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
