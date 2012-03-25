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

import java.util.Collections;
import java.util.Iterator;

/**
 * Utility class providing a unified facade for iterating over either collection
 * or arrays via reflection with a foreach looop. <br />
 * 
 * This is a utility class specially meant for merger developers so it will not
 * suffer big changes without previous notification.
 * 
 * @author juancavallotti
 */
public final class UniversalIterable<T> implements Iterable<T> {
    
    private final Object toBeIterated;
    
    /**
     * Build a universal iterable out of an array instance, or 
     * {@link java.util.Collection} instance, or maybe something implementing
     * the {@link Iterable} interface. 
     * 
     * @param toBeIterated the object to be iterated.
     */
    public UniversalIterable(Object toBeIterated) {
        this.toBeIterated = toBeIterated;
    }
    
    @Override
    public Iterator<T> iterator() {
        if (toBeIterated.getClass().isArray()) {
            return new ArrayIterator(toBeIterated);
        }
        
        if (toBeIterated instanceof Iterable) {
            return ((Iterable)toBeIterated).iterator();
        }
        
        //else we gently not explode
        return Collections.EMPTY_LIST.iterator();
    }
    
}
