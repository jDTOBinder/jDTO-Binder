package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.DTOCascade;
import com.juancavallotti.jdto.annotation.Source;
import java.util.List;

/**
 *
 * @author juancavallotti
 */
public class ComplexListDTO {
    
    //no type variabe to force reading the type variable of the method.
    @DTOCascade
    @Source("sourceList")
    private List<FormatDTO> formatDtos;

    public ComplexListDTO() {
    }

    public ComplexListDTO(List<FormatDTO> formatDtos) {
        this.formatDtos = formatDtos;
    }

    public List<FormatDTO> getFormatDtos() {
        return formatDtos;
    }

    public void setFormatDtos(List<FormatDTO> formatDtos) {
        this.formatDtos = formatDtos;
    }
       
}
