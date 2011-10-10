package com.juancavallotti.jdto;

/**
 * Merge a property into another type / form by applying a transformation. <br />
 * Transformations can be hinted by the extra param attribute.
 * @param T the type of the resulting property.
 * @param S the type of the source property, for developer convenience.
 * @author juancavallotti
 */
public interface SinglePropertyValueMerger<T, S> {

    /**
     * Merge the value of type S into another object of type T.
     * @param values the value to be merged.
     * @param extraParam metadata that may help the merger to build the result.
     * @return 
     */
    public T mergeObjects(S value, String extraParam);
}
