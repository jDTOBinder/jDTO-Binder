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

/**
 * Merge the object by calling {@link java.lang.Object#toString() toString()}. <br />
 * 
 * <ul>
 * 
 * <li>If the given parameter is null, then the merger will return null.</li>
 * <li>If the given parameter is a primitive, then the merger will return the 
 * string representation of its autoboxed counterpart.</li>
 * 
 * </ul>
 * @author Juan Alberto Lopez Cavallotti
 */
public class ToStringMerger implements SinglePropertyValueMerger<String, Object> {
    private static final long serialVersionUID = 1L;

    @Override
    public String mergeObjects(Object value, String[] extraParam) {
        //do not support toString on nulls
        if (value == null) {
            return null;
        }
        
        return value.toString();
    }
    
}
