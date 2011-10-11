package com.juancavallotti.jdto;


import com.juancavallotti.jdto.dtos.MultiSourceDTO2;
import com.juancavallotti.jdto.dtos.MultiSourceDTO;
import com.juancavallotti.jdto.entities.GeneralPurposeEntity;
import org.junit.BeforeClass;
import com.juancavallotti.jdto.entities.SimpleEntity;
import java.util.Calendar;
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
    
    
    @Test
    public void testMultiSourceDates() {
        
        SimpleEntity source1 = new SimpleEntity("hello!!", 2, 0, true);
        GeneralPurposeEntity source2 = new GeneralPurposeEntity();
        
        Calendar testDate = Calendar.getInstance();
        testDate.set(Calendar.DATE, 10);
        testDate.set(Calendar.MONTH, 1);
        testDate.set(Calendar.YEAR, 1983);
        
        source2.setTheDate(testDate.getTime());
        source2.setTheCalendar(testDate);
        
        
        MultiSourceDTO2 dto = binder.bindFromBusinessObject(MultiSourceDTO2.class, source1, source2);
        
        assertEquals(source1.getaString(), dto.getString1());
        assertEquals("02 10/02/1983", dto.getString2());
        assertEquals("10/02/1983", dto.getString3());
    }
}
