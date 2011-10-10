package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.Source;

/**
 *
 * @author juancavallotti
 */
public class SimpleAssociationDTO {
    
    @Source("myString")
    private String firstString;
    private String secondString;

    public SimpleAssociationDTO() {
    }

    public SimpleAssociationDTO(String firstString, String secondString) {
        this.firstString = firstString;
        this.secondString = secondString;
    }

    public String getFirstString() {
        return firstString;
    }

    public void setFirstString(String firstString) {
        this.firstString = firstString;
    }
    
    @Source("related.aString")
    public String getSecondString() {
        return secondString;
    }

    public void setSecondString(String secondString) {
        this.secondString = secondString;
    }
    
}
