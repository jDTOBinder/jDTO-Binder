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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdto.MultiPropertyValueMerger;

/**
 * Estimate the time between two {@link java.util.Date Dates} or {@link java.util.Calendar Calendars}.
 * <br />
 *
 * This merger works with only two sources, if less than two sources are
 * provided then a {@link IllegalArgumentException} will be thrown. <br />
 *
 * By default, this merger will estimate the amount of hours between the dates
 * but you may choose the time unit using one of the class constants. <br />
 *
 * @author Juan Alberto López Cavallotti
 * @since 1.2
 */
public class TimeBetweenDatesMerger implements MultiPropertyValueMerger<Number> {

    private static final long serialVersionUID = 1L;
    
    private static final double K_SECONDS = 1000.0;
    private static final double K_MINUTES = 60.0 * K_SECONDS;
    private static final double K_HOURS = 60.0 * K_MINUTES;
    private static final double K_DAYS = 24.0 * K_HOURS;
    private static final double K_WEEKS = 7.0 * K_DAYS;
    
    /**
     * Use this constant as a the parameter for the merger to return the time expressed in seconds.
     */
    public static final String TIME_UNIT_SECONDS = "SECONDS";
    
    /**
     * Use this constant as a the parameter for the merger to return the time expressed in minutes.
     */
    public static final String TIME_UNIT_MINUTES = "MINUTES";
    
    /**
     * Use this constant as a the parameter for the merger to return the time expressed in hours.
     */
    public static final String TIME_UNIT_HOURS = "HOURS";
    
    /**
     * Use this constant as a the parameter for the merger to return the time expressed in days.
     */
    public static final String TIME_UNIT_DAYS = "DAYS";
    
    /**
     * Use this constant as a the parameter for the merger to return the time expressed in weeks.
     */
    public static final String TIME_UNIT_WEEKS = "WEEKS";
    
    @Override
    public Number mergeObjects(List<Object> values, String[] extraParam) {
        if (values.size() < 2) {
            throw new IllegalArgumentException("Two dates or calendars are required");
        }

        Date first = readDate(values.get(0));
        Date last = readDate(values.get(1));

        if (first == null || last == null) {
            return 0;
        }
        
        long time = Math.abs(last.getTime() - first.getTime());
        
        //return the time in hours
        return adjustScale(time, extraParam);
    }
    
    /**
     * Read an object that is presumably a date and convert it.
     * @param date the date to convert.
     * @return if the argument is null, then return null.
     */
    private Date readDate(Object date) {

        if (date == null) {
            return null;
        }

        if (date instanceof Calendar) {
            return ((Calendar) date).getTime();
        }
        if (date instanceof Date) {
            return (Date) date;
        }

        throw new IllegalArgumentException("The argument must be either a date or a calendar");
    }

    private Number adjustScale(long time, String[] extraParam) {
        
        String scale = TIME_UNIT_HOURS;
        
        if (!ArrayUtils.isEmpty(extraParam)) {
            scale = extraParam[0];
        }
        
        //if statements ordered in some sense of likeliness.
        if (StringUtils.equals(scale, TIME_UNIT_HOURS)) {
            return time / K_HOURS;
        }
        if (StringUtils.equals(scale, TIME_UNIT_DAYS)) {
            return time / K_DAYS;
        }
        if (StringUtils.equals(scale, TIME_UNIT_MINUTES)) {
            return time / K_MINUTES;
        }
        if (StringUtils.equals(scale, TIME_UNIT_WEEKS)) {
            return time / K_WEEKS;
        }
        if (StringUtils.equals(scale, TIME_UNIT_SECONDS)) {
            return time / K_SECONDS;
        }
        
        //no adjustment if no proper argument.
        return time;
    }
}
