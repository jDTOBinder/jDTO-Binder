/*
 * Copyright 2013 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

/**
 * API that abstracts the information needed for DTO binding.
 * @author Juan Alberto López Cavallotti
 * @since 1.4
 */
public interface DTOMetadata {
    
    /**
     * Get a list of the constructor arguments needed for immutable DTO 
     * construction.
     * @return the field metadata for each constructor argument in proper order.
     */
    List<DTOFieldMetadata> getConstructorArgs();
    
    /**
     * Get the list of the source bean names, this is useful when binding from
     * multiple source beans.
     * @return a list of bean names.
     */
    String[] getDefaultBeanNames();

    /**
     * Get the metadata for all the fields of a non-immutable DTO.
     * @return a hash map or null if the DTO is immutable.
     */
    HashMap<String, DTOFieldMetadata> getFieldMetadata();
    
    /**
     * Get the constructor of an immutable DTO.
     * @return the given constructor or null if the DTO is not immutable.
     */
    Constructor getImmutableConstructor();
    
    /**
     * Check if the DTO is immutable or not.
     * @return true if the DTO is immutable, false otherwise.
     */
    boolean isImmutableBean();

}
