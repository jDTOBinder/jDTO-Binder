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
import org.jdto.dtos.SimpleAssociationDTO;
import org.jdto.entities.SimpleEntity;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test classes for DTO map feature.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestMapDTOs {
    
    private static DTOBinder binder;
    
    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    
    @Test
    public void testWriteMap() {
        
        SimpleEntity entity = new SimpleEntity("myString", 23, 34.45, true);
        
        HashMap<String, Object> map = binder.extractFromDto(HashMap.class, entity);
        
        
        String strVal = (String) map.get("aString");
        int intVal = (Integer) map.get("anInt");
        double doubleVal = (Double) map.get("aDouble");
        boolean boolVal = (Boolean) map.get("aBoolean");
        
        assertEquals("aString failed", entity.getaString(), strVal);
        assertEquals("anInt failed",entity.getAnInt(), intVal);
        assertEquals("aDouble failed", entity.getaDouble(), doubleVal, 0.0001);
        assertEquals("aBoolean failed", entity.isaBoolean(), boolVal);
    }
    
    @Test
    public void testReadFromMap() {
        
        HashMap<String, String> values = new HashMap<String, String>();
        
        values.put("myString", "simpleString");
        values.put("related.aString", "relatedString");
        
        SimpleAssociationDTO dto = binder.bindFromBusinessObject(SimpleAssociationDTO.class, values);
        
        assertEquals("simple attribute binding failed","simpleString",dto.getFirstString());
        assertEquals("compound attribute binding failed", "relatedString",dto.getSecondString());
    }
}
