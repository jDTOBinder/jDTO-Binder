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
package org.jdto.impl;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility methods for making values compatible one another.
 * @author Juan Alberto Lopez Cavallotti
 */
class ValueConversionHelper {
    
    /**
     * Make the target value compatible with the target type.
     * @param targetValue the target value to be converted.
     * @param targetType the target type for the value.
     * @return the value converted to the target type or the same value if no
     * conversion is available.
     */
    static Object compatibilize(Object targetValue, Class targetType) {
        
        //the f(null) = null case
        if (targetValue == null) {
            return null;
        }
        
        //if the target type is String then call toString
        if (String.class.equals(targetType)) {
            return targetValue.toString();
        }
        
        //if the target type is enum and the source is a string
        //try to convert it.
        if (targetType.isEnum() && String.class.equals(targetValue.getClass())) {
            return readEnumConstant((String)targetValue, targetType);
        }
        
        if (Date.class.equals(targetType) || Calendar.class.equals(targetType)) {
            return convertToDateOrCalendar(targetValue, targetType);
        }
        
        //enought of compatibility.
        return targetValue;
    }
    
    /**
     * Safely, try to read an enum constant and if it is not possible, then
     * return the original value.
     * @param targetValue the enum constant string.
     * @param targetType the enum type.
     * @return the enum literal or null
     */
    private static Object readEnumConstant(String targetValue, Class targetType) {
        try {
            return Enum.valueOf(targetType, targetValue);
        } catch (Exception ex) {
            return targetValue;
        }
    }

    /**
     * Safely try to convert between a date to a calendar or a calendar to a date.
     * @param targetValue
     * @param targetType
     * @return a calendar converted to date, a date converted to calendar or the same value.
     */
    private static Object convertToDateOrCalendar(Object targetValue, Class targetType) {
        
        if (targetValue instanceof Calendar) {
            return ((Calendar)targetValue).getTime();
        }
        
        if (targetValue instanceof Date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date)targetValue);
            return cal;
        }
        return targetValue;
    }
    
}
