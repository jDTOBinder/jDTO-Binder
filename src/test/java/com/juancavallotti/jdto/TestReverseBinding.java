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

package com.juancavallotti.jdto;

import com.juancavallotti.jdto.entities.AnnotatedEntity;
import com.juancavallotti.jdto.entities.SimpleEntity;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Checks the basic databinding.
 * @author juancavallotti
 */
public class TestReverseBinding {
    
    
    private static DTOBinder binder;
    
    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    /**
     * One simple way to check if everything is working fine is binding an
     * entity against itself, first we'll start with simple attributes, no
     * nested relationships and see how the framework behaves.
     */
    @Test
    public void testSimpleReverseBinding() {
        
        //create a basic entity
        SimpleEntity entity = new SimpleEntity("test", 123, 345.35, true);
        
        //try and build a DTO out of the same entity.
        SimpleEntity dto = binder.bindFromBusinessObject(SimpleEntity.class, entity);
        
        //change things on the dto
        dto.setAnInt(10);
        dto.setaBoolean(false);
        dto.setaDouble(20.20);
        dto.setaString("Changed!");
        
        entity = binder.extractFromDto(SimpleEntity.class, dto);
        
        
        assertEquals(10, entity.getAnInt());
        assertEquals(false, entity.isaBoolean());
        assertEquals(20.20, entity.getaDouble(), 0.0001);
        assertEquals("Changed!", entity.getaString());
    }

    @Test
    public void testMappedReverseBinding() {
        
        //create a basic entity
        AnnotatedEntity entity = new AnnotatedEntity("Jones", "Tom", "NOOOOO");
        
        //try and build a DTO out of the same entity.
        AnnotatedEntity dto = binder.bindFromBusinessObject(AnnotatedEntity.class, entity);
        
        //change things on the dto
        dto.setFirstString("Myers");
        dto.setSecondString("Mike");
        
        entity = binder.extractFromDto(AnnotatedEntity.class, dto);
        
        assertNull(entity.getThirdString());
        assertEquals("Mike", entity.getFirstString());
        assertEquals("Myers", entity.getSecondString());
    }
}
