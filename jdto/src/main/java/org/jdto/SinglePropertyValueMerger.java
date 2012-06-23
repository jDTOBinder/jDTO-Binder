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

package org.jdto;

import java.io.Serializable;

/**
 * Interface definig the operations needed to convert an object from type S to
 * T enabling the possibility of custom formatting and probably conditioned by 
 * the list of parameters provided. <br />
 * 
 * Merge a property into another type / form by applying a transformation. <br />
 * Transformations can be hinted by the extra param attribute.
 * 
 * @param R the type of the resulting property.
 * @param S the type of the source property, for developer convenience.
 * @author Juan Alberto Lopez Cavallotti
 * @serial 1.0
 */
public interface SinglePropertyValueMerger<R, S> extends PropertyValueMerger,Serializable {

    /**
     * Merge the value of type S into another object of type R.
     * @param value the value to be merged.
     * @param extraParam metadata that may help the merger to build the result.
     * @return the merge resulting object.
     */
    public R mergeObjects(S value, String[] extraParam);
    
    
    /**
     * Check if this merger can be used for unmerging a given value with the 
     * given parameters. If this method returns false then the 
     * {@link #unmergeObject(java.lang.Object, java.lang.String[])}
     * method must return null. <br />
     * 
     * @param params The parameters that will be passed to the unmerge method.
     * 
     * @return true if the unmerge operation is supported, false if not.
     * @since 1.2.
     */
    public boolean isUnmergeSupported(String[] params);
    
    /**
     * Try to recover the original value form this merger, this operation may not
     * be always available, you may call {@link #isUnmergeSupported()} to check 
     * if this type of merger is reversible or not. <br />
     * 
     * If this merger is not reversible, then this method must return null. <br />
     * 
     * @param object the object to unmerge.
     * @param params the parameters passed in to the merge process.
     * @return an unmerged object or null if something fails.
     * @since 1.2
     */
    public S unmergeObject(R object, String[] params);
}
