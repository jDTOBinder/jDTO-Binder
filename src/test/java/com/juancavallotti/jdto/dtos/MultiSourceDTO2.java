package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.Source;
import com.juancavallotti.jdto.annotation.SourceNames;
import com.juancavallotti.jdto.annotation.Sources;
import com.juancavallotti.jdto.mergers.DateFormatMerger;
import com.juancavallotti.jdto.mergers.StringFormatMerger;

/**
 * A second multi source dto
 * @author juancavallotti
 */
@SourceNames({"bean1", "bean2"})
public class MultiSourceDTO2 {
    
    
    @Source("aString") //using bean1 as default
    private String string1;
    
    @Sources(value={@Source("anInt"), @Source(value = "theDate", sourceBean="bean2", merger=DateFormatMerger.class, mergerParam="dd/MM/yyyy")}, merger=StringFormatMerger.class, mergerParam="%02d %s")
    private String string2;
    
    
    @Source(value = "theCalendar", sourceBean="bean2", merger=DateFormatMerger.class, mergerParam="dd/MM/yyyy")
    private String string3;

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }
    
    
}
