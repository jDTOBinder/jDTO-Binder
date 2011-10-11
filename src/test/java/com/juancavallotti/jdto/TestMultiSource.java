package com.juancavallotti.jdto;


import com.juancavallotti.jdto.dtos.MultiSourceDTO;
import org.junit.BeforeClass;
import com.juancavallotti.jdto.entities.SimpleEntity;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author juancavallotti
 */
public class TestMultiSource {
    
    private static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    @Test
    public void testMultiSource() {
        
        //try to merge 3 beans into just one dto.
        SimpleEntity source1 = new SimpleEntity("hello", 0, 0, true);
        SimpleEntity source2 = new SimpleEntity("cruel", 0, 0, true);
        SimpleEntity source3 = new SimpleEntity("world", 0, 0, true);
        
        MultiSourceDTO dto = binder.bindFromBusinessObject(MultiSourceDTO.class, source1, source2, source3);
        
        assertEquals("hello", dto.getSource1());
        assertEquals("cruel", dto.getSource2());
        assertEquals("world", dto.getSource3());
    }
    
}
