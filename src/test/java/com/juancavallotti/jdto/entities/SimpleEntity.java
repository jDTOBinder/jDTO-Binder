/*
 *    Copyright 2011 Juan Alberto López Cavallotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
