/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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
import java.util.Set;
import org.jdto.entities.ComplexArray;
import org.jdto.dtos.ComplexArrayDTO;
import org.jdto.dtos.ComplexDTO;
import org.jdto.dtos.ComplexListDTO;
import org.jdto.entities.ComplexEntity;
import org.jdto.entities.ComplexList;
import org.jdto.entities.ComplexSet;
import org.jdto.entities.SimpleAssociation;
import org.jdto.entities.SimpleEntity;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestCascadeBinding {

    private static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    @Test
    public void testBasicCascade() {
        
        //this shouldnt cascade
        SimpleEntity simple = new SimpleEntity("I'm string", 10, 10.12, true);
        //this should cascade
        SimpleAssociation associated = new SimpleAssociation(simple, "i'm associated");
        //this is a complex entity :)
        ComplexEntity source = new ComplexEntity("i'm complex", associated, 20);
        
        ComplexDTO dto = binder.bindFromBusinessObject(ComplexDTO.class, source);
        
        assertEquals(source.getName(), dto.getStringField());
        assertNotNull(dto.getCascadedField());
        assertEquals(associated.getMyString(), dto.getCascadedField().getFirstString());
        assertEquals(simple.getaString(), dto.getCascadedField().getSecondString());
    }
    
    @Test
    public void testCascadeList() {
        
        ArrayList<SimpleEntity> values = new ArrayList<SimpleEntity>();
        
        values.add(new SimpleEntity("first", 1, 1.1, true));
        values.add(new SimpleEntity("Second", 2, 2.2, false));
        
        ComplexList list = new ComplexList(values);
        
        ComplexListDTO cldto = binder.bindFromBusinessObject(ComplexListDTO.class, list);
        
        assertNotNull(cldto.getFormatDtos());
        assertTrue(cldto.getFormatDtos().size() == 2);
    }

    @Test
    public void testCascadeListArray() {
        
        ArrayList<SimpleEntity> values = new ArrayList<SimpleEntity>();
        
        values.add(new SimpleEntity("first", 1, 1.1, true));
        values.add(new SimpleEntity("Second", 2, 2.2, false));
        
        ComplexList list = new ComplexList(values);
        
        ComplexArrayDTO cldto = binder.bindFromBusinessObject(ComplexArrayDTO.class, list);
        
        assertNotNull(cldto.getFormatDtos());
        assertTrue(cldto.getFormatDtos().length == 2);
    }
    @Test
    public void testCascadeArrayArray() {
        
        SimpleEntity[] values = new SimpleEntity[2];
        
        values[0] = (new SimpleEntity("first", 1, 1.1, true));
        values[1] = (new SimpleEntity("Second", 2, 2.2, false));
        
        ComplexArray list = new ComplexArray(values);
        
        ComplexArrayDTO cldto = binder.bindFromBusinessObject(ComplexArrayDTO.class, list);
        
        assertNotNull(cldto.getFormatDtos());
        assertTrue(cldto.getFormatDtos().length == 2);
    }
    @Test
    public void testCascadeSetArray() {
        
        Set<SimpleEntity> values = new HashSet<SimpleEntity>();
        
        values.add(new SimpleEntity("first", 1, 1.1, true));
        values.add(new SimpleEntity("Second", 2, 2.2, false));
        
        ComplexSet list = new ComplexSet(values);
        
        ComplexArrayDTO cldto = binder.bindFromBusinessObject(ComplexArrayDTO.class, list);
        
        assertNotNull(cldto.getFormatDtos());
        assertTrue(cldto.getFormatDtos().length == 2);
    }
}
