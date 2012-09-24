/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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

package org.jdto.mergers.dtos;

import java.io.Serializable;

/**
 * Integration object to test the groovy mergers.
 * 
 * @author Juan Alberto López Cavallotti
 */
public class GroovyBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String myString;

    public GroovyBean() {
    }

    public GroovyBean(String myString) {
        this.myString = myString;
    }
    
    
    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }
    
}
