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
import org.jdto.MultiPropertyValueMerger;
import org.jdto.SinglePropertyValueMerger;

import java.util.Arrays;
import java.util.List;

/**
 * This merger takes the formatting string (see {@link String#format(java.lang.String, java.lang.Object[]) })
 * as the merger extra parameter and returns a String. <br />
 * 
 * For more information see {@link org.jdto.annotation.Source#merger() } and
 * {@link org.jdto.annotation.Sources#merger() } documentation. <br />
 * 
 * You must provide a format string as the merger param. <br />
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class StringFormatMerger implements MultiPropertyValueMerger<String>, SinglePropertyValueMerger<String, Object> {
    private static final long serialVersionUID = 1L;
    
    /**
     * Merges the objects using the extraParam as a formatting String.
     * @param values the value list of objects to be merged.
     * @param extraParams the format string.
     * @return a string for the provided format.
     */
    @Override
    public String mergeObjects(List<Object> values, String[] extraParams) {
        
        if (ArrayUtils.isEmpty(extraParams)) {
            throw new IllegalArgumentException("Format String is required");
        }
        
        String extraParam = extraParams[0];
        Object[] params = values.toArray();
        return String.format(extraParam, params);
    }
    
    /**
     * Delegate to the mergeObjects method.
     * @param value the value to format.
     * @param extraParam the format string.
     * @return a string for the provided format.
     */
    @Override
    public String mergeObjects(Object value, String[] extraParam) {
        return mergeObjects(Arrays.asList(value), extraParam);
    }

    @Override
    public boolean isRestoreSupported(String[] params) {
        return false; //coiuld be fun, but no
    }

    @Override
    public Object restoreObject(String object, String[] params) {
        return null;
    }
    
}
