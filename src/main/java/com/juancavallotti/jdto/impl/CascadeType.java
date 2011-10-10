package com.juancavallotti.jdto.impl;

/**
 * Describe the cascade types that can be applied to DTOs
 * @author juancavallotti
 */
public enum CascadeType {
    /**
     * Single field conversion
     */
    SINGLE,
    /**
     * Arrays should be treated as lists and then converted to arrays again.
     */
    ARRAY,
    /**
     * The only collection supported at the moment will be the list but for
     * futures sake.
     */
    COLLECTION
}
