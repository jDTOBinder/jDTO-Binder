package com.juancavallotti.jdto.impl.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator to iterate reflectively over an instance of an array.
 * Use this Iterator only if you are not able to iterate over an array any other
 * way.
 * @author juancavallotti
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
