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

import java.lang.reflect.Method;
import org.jdto.SinglePropertyValueMerger;

/**
 * Merge the object by calling {@link java.lang.Object#clone() clone()}. <br />
 * 
 * <ul>
 * 
 * <li>If the given parameter is null, then the merger will return null.</li>
 * <li>If the given parameter is a primitive, then the merger will return the 
 * cloned representation of its autoboxed counterpart.</li>
 * <li>If the object does not implement {@link java.lang.Cloneable Cloneable} 
 * then the merger will return null.</li>
 * </ul>
 * @author Juan Alberto Lopez Cavallotti
 */
public class CloneMerger implements SinglePropertyValueMerger<Object, Object> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Merge the object by trying to clone it.
     * @param value the value to be cloned.
     * @param extraParam no parameter is supported.
     * @return A clone of the object or null if clone is not supported.
     */
    @Override
    public Object mergeObjects(Object value, String[] extraParam) {
        if (value == null) {
            return null;
        }
        
        if (!(value instanceof Cloneable)) {
            return null;
        }
        
        try {
            Method cloneMethod = value.getClass().getMethod("clone");
            //clone.
            return cloneMethod.invoke(value);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean isUnmergeSupported(String[] params) {
        return false; //I cant return the orginal object from a clone, just another clone.
    }

    @Override
    public Object unmergeObject(Object object, String[] params) {
        return null;
    }
}
