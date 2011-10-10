package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.Source;
import com.juancavallotti.jdto.annotation.Sources;
import com.juancavallotti.jdto.mergers.StringFormatMerger;

/**
 *
 * @author juancavallotti
 */
public class FormatDTO {
    
    @Source(value="aDouble", merger=StringFormatMerger.class, mergerParam="$ %.2f")
    private String price;
    
    @Sources(value={@Source("aDouble"), @Source("anInt")}, 
            merger=StringFormatMerger.class, mergerParam="%.2f %08d")
    private String compound;

    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
}
