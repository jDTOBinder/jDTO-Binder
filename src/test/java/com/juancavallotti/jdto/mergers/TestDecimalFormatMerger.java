package com.juancavallotti.jdto.mergers;

import java.text.DecimalFormat;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 *
 * @author juancavallotti
 */
public class TestDecimalFormatMerger {
    
    @Test
    public void testDecimalFormatMerger() {
        
        
        DecimalFormatMerger merger = new DecimalFormatMerger();
        
        double value = 0.101;
        
        String result = merger.mergeObjects(value, "% #,###.00");
        
        assertEquals(new DecimalFormat("% #,###.00").format(value), result);
    }
}
