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

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

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
     * @param extraParams
     * @return 
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
     * @param value
     * @param extraParam
     * @return 
     */
    @Override
    public String mergeObjects(Object value, String[] extraParam) {
        return mergeObjects(Arrays.asList(value), extraParam);
    }
    
}
