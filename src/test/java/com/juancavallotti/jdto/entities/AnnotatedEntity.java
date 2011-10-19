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

import com.juancavallotti.jdto.annotation.DTOTransient;
import com.juancavallotti.jdto.annotation.Source;

/**
 *
 * @author juancavallotti
 */
public class AnnotatedEntity {

    public AnnotatedEntity() {
    }

    public AnnotatedEntity(String firstString, String secondString, String thirdString) {
        this.firstString = firstString;
        this.secondString = secondString;
        this.thirdString = thirdString;
    }
    
    @Source("secondString")
    private String firstString;
    @Source("firstString")
    private String secondString;
    
    @DTOTransient
    private String thirdString;

    public String getFirstString() {
        return firstString;
    }

    public void setFirstString(String firstString) {
        this.firstString = firstString;
    }

    public String getSecondString() {
        return secondString;
    }

    public void setSecondString(String secondString) {
        this.secondString = secondString;
    }

    public String getThirdString() {
        return thirdString;
    }

    public void setThirdString(String thirdString) {
        this.thirdString = thirdString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnnotatedEntity other = (AnnotatedEntity) obj;
        if ((this.firstString == null) ? (other.firstString != null) : !this.firstString.equals(other.firstString)) {
            return false;
        }
        if ((this.secondString == null) ? (other.secondString != null) : !this.secondString.equals(other.secondString)) {
            return false;
        }
        if ((this.thirdString == null) ? (other.thirdString != null) : !this.thirdString.equals(other.thirdString)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.firstString != null ? this.firstString.hashCode() : 0);
        hash = 83 * hash + (this.secondString != null ? this.secondString.hashCode() : 0);
        hash = 83 * hash + (this.thirdString != null ? this.thirdString.hashCode() : 0);
        return hash;
    }
    
}
