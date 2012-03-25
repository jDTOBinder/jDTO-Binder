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

package org.jdto.entities;

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
