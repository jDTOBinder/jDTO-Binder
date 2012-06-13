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

import java.util.ArrayList;
import org.jdto.dtos.PersonAddressDTO;
import org.jdto.dtos.PersonDTO;
import org.jdto.entities.Person;
import org.jdto.entities.PersonAddress;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Test cases for the cycle avoidance logic.
 * @author Juan Alberto López Cavallotti
 */
public class TestCycleAvoidance {
    
    private static DTOBinder binder;
    
    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    @Test
    public void testCycleHandling() {
        
        Person p = new Person();
        p.setName("John Doe");
        
        ArrayList<PersonAddress> addrs = new ArrayList<PersonAddress>();
        
        PersonAddress addr = new PersonAddress();
        addr.setPerson(p);
        addr.setAddress("Addr 1");
        
        addrs.add(addr);
        
        addr = new PersonAddress();
        addr.setPerson(p);
        addr.setAddress("Addr 2");
        
        p.setAddresses(addrs);
        
        PersonDTO dto = binder.bindFromBusinessObject(PersonDTO.class, p);
        
        assertNotNull("The dto should have been bound correctly.",dto);
        assertNotNull("The dto should have addresses", dto.getAddresses());
        assertEquals("The name property should be the same", p.getName(), dto.getName());
        assertEquals("The dto should have the same addresses.",p.getAddresses().size(),  dto.getAddresses().size());
        
        for (PersonAddressDTO personAddressDTO : dto.getAddresses()) {
            assertEquals("The dto should contain the person", dto, personAddressDTO.getPerson());
        }
        
    }
}
