package com.juancavallotti.jdto.entities;

/**
 *
 * @author juancavallotti
 */
public class ComplexArray {
    
    private SimpleEntity[] sourceList;

    public ComplexArray() {
    }

    public ComplexArray(SimpleEntity[] sourceList) {
        this.sourceList = sourceList;
    }

    public SimpleEntity[] getSourceList() {
        return sourceList;
    }

    public void setSourceList(SimpleEntity[] sourceList) {
        this.sourceList = sourceList;
    }

}
