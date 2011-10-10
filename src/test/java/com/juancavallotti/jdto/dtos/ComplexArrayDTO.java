package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.DTOCascade;
import com.juancavallotti.jdto.annotation.Source;

/**
 *
 * @author juancavallotti
 */
public class ComplexArrayDTO {
    
    //no type variabe to force reading the type variable of the method.
    @DTOCascade
    @Source("sourceList")
    private FormatDTO[] formatDtos;

    public ComplexArrayDTO() {
    }

    public ComplexArrayDTO(FormatDTO[] formatDtos) {
        this.formatDtos = formatDtos;
    }

    public FormatDTO[] getFormatDtos() {
        return formatDtos;
    }

    public void setFormatDtos(FormatDTO[] formatDtos) {
        this.formatDtos = formatDtos;
    }

}
