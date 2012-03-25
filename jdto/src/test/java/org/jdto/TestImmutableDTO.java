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
import org.jdto.entities.SimpleAssociation;
import org.jdto.dtos.SimpleImmutableDTO;
import org.jdto.entities.SimpleEntity;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Checks immutable dto binding health
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestImmutableDTO {
    
    
    private static DTOBinder binder;
    
    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    @Test
    public void testSimpleAssociationDTOBinding() {
        SimpleEntity simpleBusinessObject = new SimpleEntity("simple", 123, 345.35, true);
        SimpleAssociation entity = new SimpleAssociation(simpleBusinessObject, "related");
        
        SimpleImmutableDTO dto = binder.bindFromBusinessObject(SimpleImmutableDTO.class, entity);
        
        //the other fields shouldnt be null
        assertEquals("second string is mapped with related",entity.getRelated().getaString(), dto.getSecondString());
        assertEquals("first string is mapped with myString",entity.getMyString(), dto.getFirstString());
    }
    
}
