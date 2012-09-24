/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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

package org.jdto;


import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.dtos.MultiSourceDTO2;
import org.jdto.dtos.MultiSourceDTO;
import org.jdto.entities.GeneralPurposeEntity;
import org.junit.BeforeClass;
import org.jdto.entities.SimpleEntity;
import java.util.Calendar;
import org.jdto.dtos.MultiSourceDTO3;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Juan Alberto Lopez Cavallotti
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
        
        SimpleEntity source1 = new SimpleEntity("hello!!", 4, 0, true);
        GeneralPurposeEntity source2 = new GeneralPurposeEntity();
        
        Calendar testDate = Calendar.getInstance();
        testDate.set(Calendar.DATE, 10);
        testDate.set(Calendar.MONTH, 1);
        testDate.set(Calendar.YEAR, 1983);
        
        source2.setTheDate(testDate.getTime());
        source2.setTheCalendar(testDate);
        
        
        MultiSourceDTO2 dto = binder.bindFromBusinessObject(MultiSourceDTO2.class, source1, source2);
        
        assertEquals(source1.getaString(), dto.getString1());
        assertEquals("04 10/02/1983", dto.getString2());
        assertEquals("10/02/1983", dto.getString3());
    }
    
    @Test
    public void testSameBean() {
        SimpleEntity entity1 = new SimpleEntity("Hello", 0, 0, false);
        SimpleEntity entity2 = new SimpleEntity("World", 0, 0, false);
        
        MultiSourceDTO3 dto = binder.bindFromBusinessObject(MultiSourceDTO3.class, entity1, entity2);
        
        assertEquals("Hello and World!", dto.getTestString());
    }
}
