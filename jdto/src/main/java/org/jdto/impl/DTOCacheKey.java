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
package org.jdto.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;

/**
 * Wrapper object to allow the caching of DTO results.
 * @author Juan Alberto López Cavallotti
 */
final class DTOCacheKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final BeanMetadata metadata;
    private final Object[] array;

    public DTOCacheKey(BeanMetadata metadata, Object[] array) {
        this.metadata = metadata;
        this.array = array;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        
        if (!obj.getClass().equals(getClass())) {
            return false;
        }
        
        DTOCacheKey other = (DTOCacheKey) obj;
        
        return ObjectUtils.equals(metadata, other.metadata) &&
                ArrayUtils.isEquals(array, other.array);
    }

    @Override
    public int hashCode() {
        
        int mdhc = ObjectUtils.hashCode(metadata);
        int ahc = ArrayUtils.hashCode(array);
        
        return mdhc + ahc;
    }
    
}
