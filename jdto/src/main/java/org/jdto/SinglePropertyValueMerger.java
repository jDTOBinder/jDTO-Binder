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

/**
 * Merge a property into another type / form by applying a transformation. <br />
 * Transformations can be hinted by the extra param attribute.
 * @param R the type of the resulting property.
 * @param S the type of the source property, for developer convenience.
 * @author Juan Alberto Lopez Cavallotti
 */
public interface SinglePropertyValueMerger<R, S> {

    /**
     * Merge the value of type S into another object of type R.
     * @param value the value to be merged.
     * @param extraParam metadata that may help the merger to build the result.
     * @return the merge resulting object.
     */
    public R mergeObjects(S value, String[] extraParam);
}
