package com.juancavallotti.jdto.entities;

/**
 *
 * @author juancavallotti
 */
public class ComplexEntity {
    
    private String name;
    
    private SimpleAssociation association;
    
    private int noDto;

    public ComplexEntity() {
    }

    public ComplexEntity(String name, SimpleAssociation association, int noDto) {
        this.name = name;
        this.association = association;
        this.noDto = noDto;
    }

    public SimpleAssociation getAssociation() {
        return association;
    }

    public void setAssociation(SimpleAssociation association) {
        this.association = association;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoDto() {
        return noDto;
    }

    public void setNoDto(int noDto) {
        this.noDto = noDto;
    }
    
}
