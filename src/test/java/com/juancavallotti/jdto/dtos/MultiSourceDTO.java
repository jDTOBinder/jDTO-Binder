package com.juancavallotti.jdto.dtos;

import com.juancavallotti.jdto.annotation.Source;
import com.juancavallotti.jdto.annotation.SourceNames;

/**
 *
 * @author juancavallotti
 */
@SourceNames({"bean1", "bean2", "bean3"})
public class MultiSourceDTO {
    
    
    @Source(value="aString", sourceBean="bean1")
    private String source1;
    @Source(value="aString", sourceBean="bean2")
    private String source2;
    @Source(value="aString", sourceBean="bean3")
    private String source3;

    public String getSource1() {
        return source1;
    }

    public void setSource1(String source1) {
        this.source1 = source1;
    }

    public String getSource2() {
        return source2;
    }

    public void setSource2(String source2) {
        this.source2 = source2;
    }

    public String getSource3() {
        return source3;
    }

    public void setSource3(String source3) {
        this.source3 = source3;
    }
    
}
