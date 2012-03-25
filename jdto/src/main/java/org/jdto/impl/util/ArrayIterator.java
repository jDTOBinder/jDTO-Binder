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

package org.jdto.impl.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator to iterate reflectively over an instance of an array.
 * Use this Iterator only if you are not able to iterate over an array any other
 * way.
 * @author Juan Alberto Lopez Cavallotti
 */
public final class ArrayIterator<T> implements Iterator<T> {
    
    private final Object array;
    
    int cursor = 0;
    
    /**
     * Build a new instance of ArrayIterator out of an Array instance.
     * @param array 
     */
    public ArrayIterator(Object array) {
        this.array = array;
    }
    
    @Override
    public boolean hasNext() {
        return cursor < Array.getLength(array);
    }

    @Override
    public T next() {
        
        if (!hasNext()) {
            throw new NoSuchElementException("Array has no more elements.");
        }
        
        return (T) Array.get(array, cursor++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Array has immutable length");
    }
    
}
