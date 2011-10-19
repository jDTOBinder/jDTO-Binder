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

package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.DTOCascade;
import com.juancavallotti.jdto.annotation.Source;

/**
 *
 * @author juancavallotti
 */
public class ComplexDTO {
    private String stringField;
    
    private SimpleAssociationDTO cascadedField;

    @DTOCascade
    @Source("association")
    public SimpleAssociationDTO getCascadedField() {
        return cascadedField;
    }

    public void setCascadedField(SimpleAssociationDTO cascadedField) {
        this.cascadedField = cascadedField;
    }
    
    @Source("name")
    public String getStringField() {
        return stringField;
    }
    
    public void setStringField(String stringField) {
        this.stringField = stringField;
    }
    
}
