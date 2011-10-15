package com.juancavallotti.jdto.dtos;

import java.io.Serializable;

/**
 *
 * @author juancavallotti
 */
public class XMLTesterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String dtoName;

    public String getDtoName() {
        return dtoName;
    }

    public void setDtoName(String dtoName) {
        this.dtoName = dtoName;
    }
    
    
}
