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

import org.jdto.dtos.CompatibilityDTO;
import org.jdto.dtos.UsefulEnum;
import org.jdto.entities.GeneralPurposeEntity;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the compatibility logic.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestCompatibilityLogic {

    private static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    @Test
    public void testCompatibility() {
        GeneralPurposeEntity gpe = new GeneralPurposeEntity();
        gpe.setTheInt(210);
        gpe.setTheString("USELESS");
        
        CompatibilityDTO dto = binder.bindFromBusinessObject(CompatibilityDTO.class, gpe);
        
        assertEquals("Should be the srting representation of an integer", ((Integer) gpe.getTheInt()).toString(), dto.getStrRep());
        assertEquals("Enum constant should be present and right", UsefulEnum.USELESS, dto.getUsefulEnum());
    }
}
