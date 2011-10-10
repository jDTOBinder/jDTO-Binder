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
