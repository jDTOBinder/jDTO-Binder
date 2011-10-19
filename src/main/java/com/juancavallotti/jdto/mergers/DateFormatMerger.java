/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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
    @Override
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
