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

package org.jdto.mergers;

import org.jdto.SinglePropertyValueMerger;

/**
 * Returns the same instance it gets as parameter. This is the default property
 * merger, it just keeps the original value
 * as it is. <br />
 * 
 * This behavior is helpful when the developer wants the value to remain the same
 * across the copy process. <br />
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public final class IdentityPropertyValueMerger implements SinglePropertyValueMerger<Object, Object> {
    
    /**
     * Return the same value it gets as parameter.
     * @param value the value to be retuned.
     * @param extraParam ignored.
     * @return the same value received.
     */
    @Override
    public Object mergeObjects(Object value, String[] extraParam) {
        return value;
    }
    
}
