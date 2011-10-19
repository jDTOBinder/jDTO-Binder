package com.juancavallotti.jdto.impl.util;

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
