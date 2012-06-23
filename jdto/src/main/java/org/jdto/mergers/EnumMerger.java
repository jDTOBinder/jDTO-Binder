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
 * 
 * Convert an enum constant to its string representation by calling the 
 * {@link java.lang.Enum#name() name()} method. This will return the string
 * representation of the enum constant.
 * 
 * Discussion: Since the name() method is not preceded with "get", then you 
 * cannot convert the enum constant to a string directly. On the other hand, you
 * may call toString() but the documentation encourages the user to override
 * that method instead of name() so it always return the string representation of
 * the enum constant.
 * 
 * <ul>
 * 
 * <li>If the parameter is null, then the merger will return null</li>
 * <li>If the parameter is not an enum constant, then throw 
 * {@link ClassCastException}. </li>
 * 
 * </ul>
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class EnumMerger implements SinglePropertyValueMerger<String, Enum> {
    private static final long serialVersionUID = 1L;
    
    /**
     * Return the name of the enum constant.
     * @param value the enum constant to get the name.
     * @param extraParam not used.
     * @return the result of invoking name() on the enum constant.
     */
    @Override
    public String mergeObjects(Enum value, String[] extraParam) {
        
        if (value == null) {
            return null;
        }
        
        return value.name();
    }

    @Override
    public boolean isUnmergeSupported(String[] params) {
        //even though I can make it here, we have more information
        //in the value compatibilization process.
        return false;
    }

    @Override
    public Enum unmergeObject(String object, String[] params) {
        return null;
    }
    
}
