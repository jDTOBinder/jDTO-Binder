package com.juancavallotti.jdto.entities;

import java.util.Set;

/**
 *
 * @author juancavallotti
 */
public class ComplexSet {
    
    private Set<SimpleEntity> sourceList;

    public ComplexSet() {
    }

    public ComplexSet(Set<SimpleEntity> sourceList) {
        this.sourceList = sourceList;
    }

    public Set<SimpleEntity> getSourceList() {
        return sourceList;
    }

    public void setSourceList(Set<SimpleEntity> sourceList) {
        this.sourceList = sourceList;
    }

}
