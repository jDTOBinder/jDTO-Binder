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
import com.juancavallotti.jdto.dtos.FormatDTO;
import com.juancavallotti.jdto.entities.SimpleAssociation;
import com.juancavallotti.jdto.dtos.SimpleAssociationDTO;
import com.juancavallotti.jdto.entities.SimpleEntity;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Checks the basic databinding.
 * @author juancavallotti
 */
public class TestBasicBinding {
    
    
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
    public void testSimpleSelfBinding() {
        
        //create a basic entity
        SimpleEntity entity = new SimpleEntity("test", 123, 345.35, true);
        
        //try and build a DTO out of the same entity.
        SimpleEntity dto = binder.bindFromBusinessObject(SimpleEntity.class, entity);
        
        assertNotSame("Entities should not be the same instance", entity, dto);
        assertEquals("Both entities should be equals", entity, dto);
    }
    
    @Test
    public void testTransientSelfBinding() {
        AnnotatedEntity entity = new AnnotatedEntity("first", "second", "third");
        
        AnnotatedEntity dto = binder.bindFromBusinessObject(AnnotatedEntity.class, entity);
        
        assertNull("third field is transient and should be null", dto.getThirdString());
        
        //the other fields shouldnt be null
        assertNotNull(dto.getFirstString());
        assertNotNull(dto.getSecondString());
    }

    @Test
    public void testSimpleSourceSelfBinding() {
        AnnotatedEntity entity = new AnnotatedEntity("first", "second", "third");
        
        AnnotatedEntity dto = binder.bindFromBusinessObject(AnnotatedEntity.class, entity);
        
        assertNull("third field is transient and should be null", dto.getThirdString());
        
        //the other fields shouldnt be null
        assertEquals("first is mapped with second",entity.getFirstString(), dto.getSecondString());
        assertEquals("second is mapped with firs",entity.getSecondString(), dto.getFirstString());
    }
    
    
    @Test
    public void testSimpleAssociationDTOBinding() {
        SimpleEntity simpleBusinessObject = new SimpleEntity("simple", 123, 345.35, true);
        SimpleAssociation entity = new SimpleAssociation(simpleBusinessObject, "related");
        
        SimpleAssociationDTO dto = binder.bindFromBusinessObject(SimpleAssociationDTO.class, entity);
        
        //the other fields shouldnt be null
        assertEquals("second string is mapped with related",entity.getRelated().getaString(), dto.getSecondString());
        assertEquals("first string is mapped with myString",entity.getMyString(), dto.getFirstString());
    }

    @Test
    public void testComplexConverterDTOBinding() {
        SimpleEntity simpleBusinessObject = new SimpleEntity("simple", 123, 345.35222, true);
        
        String expectedPrice = String.format("$ %.2f", simpleBusinessObject.getaDouble());
        String expectedCompound = String.format("%.2f %08d", simpleBusinessObject.getaDouble(), simpleBusinessObject.getAnInt());
        
        
        FormatDTO dto = binder.bindFromBusinessObject(FormatDTO.class, simpleBusinessObject);
        
        assertNotNull("double format attribute should not be null", dto.getPrice());
        assertNotNull("compound format attribute should not be null", dto.getCompound());
        
        assertEquals(expectedPrice, dto.getPrice());
        assertEquals(expectedCompound, dto.getCompound());
    }
    
}
