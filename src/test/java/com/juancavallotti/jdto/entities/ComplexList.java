package com.juancavallotti.jdto.entities;

import java.util.ArrayList;

/**
 *
 * @author juancavallotti
 */
public class ComplexList {
    
    private ArrayList<SimpleEntity> sourceList;

    public ComplexList() {
    }

    public ComplexList(ArrayList<SimpleEntity> sourceList) {
        this.sourceList = sourceList;
    }

    public ArrayList<SimpleEntity> getSourceList() {
        return sourceList;
    }

    public void setSourceList(ArrayList<SimpleEntity> sourceList) {
        this.sourceList = sourceList;
    }

}
