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

import org.jdto.SinglePropertyValueMerger;
import java.text.DecimalFormat;
import org.apache.commons.lang.ArrayUtils;

/**
 * Format a given number using a {@link DecimalFormat} instance with the format
 * string as the extra param. Please refer to the {@link DecimalFormat} documentation
 * to check how to write format Strings.
 * 
 * You must provide a format string as the extraParam, otherwise the an IllegalArgumentException
 * will be thrown. <br />
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class DecimalFormatMerger implements SinglePropertyValueMerger<String, Number> {
    private static final long serialVersionUID = 1L;
    
    /**
     * Merge a number by using a {@link DecimalFormat} instance.
     * @param value the number to be formatted.
     * @param extraParam the format string.
     * @return the merged object formatted with JDK decimal format.
     */
    @Override
    public String mergeObjects(Number value, String[] extraParam) {
        
        if (ArrayUtils.isEmpty(extraParam)) {
            throw new IllegalArgumentException("Number format param is required");
        }
        
        
        DecimalFormat format = new DecimalFormat(extraParam[0]);
        
        return format.format(value);
    }
    
}
