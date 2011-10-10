package com.juancavallotti.jdto.entities;

/**
 * Simple entity for basic DTO binding testing.
 * @author juancavallotti
 */
public class SimpleEntity {
    private String aString;
    private int anInt;
    private double aDouble;
    private boolean aBoolean;
    
    public SimpleEntity() {
    }

    public SimpleEntity(String aString, int anInt, double aDouble, boolean aBoolean) {
        this.aString = aString;
        this.anInt = anInt;
        this.aDouble = aDouble;
        this.aBoolean = aBoolean;
    }


    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
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

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleEntity other = (SimpleEntity) obj;
        if ((this.aString == null) ? (other.aString != null) : !this.aString.equals(other.aString)) {
            return false;
        }
        if (this.anInt != other.anInt) {
            return false;
        }
        if (Double.doubleToLongBits(this.aDouble) != Double.doubleToLongBits(other.aDouble)) {
            return false;
        }
        if (this.aBoolean != other.aBoolean) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.aString != null ? this.aString.hashCode() : 0);
        hash = 71 * hash + this.anInt;
        hash = 71 * hash + (int) (Double.doubleToLongBits(this.aDouble) ^ (Double.doubleToLongBits(this.aDouble) >>> 32));
        hash = 71 * hash + (this.aBoolean ? 1 : 0);
        return hash;
    }
    
}
