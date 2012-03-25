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

package org.jdto;

import java.util.List;

/**
 * Implementations should know how to merge a list of objects into a single object. <br />
 * This interface is meant to be used to create a single value out of a multi-source
 * property configuration, see {@link com.juancavallotti.jdto.annotation.Sources}.
 * @param <R> The result type of the merged parameters.
 * @author juancavallotti
 */
public interface MultiPropertyValueMerger<R> {
    
    /**
     * Merge the list of objects into a single object.
     * @param values the values to be merged.
     * @param extraParam metadata that may help the merger to build the result.
     * @return the merge resulting object.
     */
    public R mergeObjects(List<Object> values, String[] extraParam);
}
