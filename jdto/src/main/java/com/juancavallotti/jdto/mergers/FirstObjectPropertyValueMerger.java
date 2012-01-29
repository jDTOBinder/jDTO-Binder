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
import java.util.List;

/**
 * This is the default property value merger. <br />
 * It returns the first non null value of the list.
 * 
 * @author juancavallotti
 */
public class FirstObjectPropertyValueMerger implements MultiPropertyValueMerger<Object> {

    /**
     * Return the first non-null value of the list or null if none found.
     * @param values
     * @return the first object on the list.
     */
    @Override
    public Object mergeObjects(List<Object> values, String[] extraParam) {
        for (Object object : values) {
            if (object != null) {
                return object;
            }
        }
        return null;
    }
    
}
