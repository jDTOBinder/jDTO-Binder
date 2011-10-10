package com.juancavallotti.jdto.entities;

/**
 * Simple business object with association.
 * @author juancavallotti
 */
public class SimpleAssociation {
    
    private SimpleEntity related;
    
    private String myString;

    public SimpleAssociation() {
    }

    public SimpleAssociation(SimpleEntity related, String myString) {
        this.related = related;
        this.myString = myString;
    }

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public SimpleEntity getRelated() {
        return related;
    }

    public void setRelated(SimpleEntity related) {
        this.related = related;
    }
    
}
