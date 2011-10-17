package com.juancavallotti.jdto.dtos;

import java.io.Serializable;

/**
 *
 * @author juancavallotti
 */
public class XMLTesterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String dtoName;
    private String aString;
    private int anInt;
        
    public String getDtoName() {
        return dtoName;
    }

    public void setDtoName(String dtoName) {
        this.dtoName = dtoName;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }
    
}
