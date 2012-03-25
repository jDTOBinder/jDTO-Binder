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
package org.jdto.impl.xml;

import org.jdto.dtos.SimpleImmutableDTO;
import org.jdto.dtos.MultiSourceDTO;
import org.jdto.dtos.FormatDTO;
import org.jdto.dtos.ComplexDTO;
import org.jdto.entities.ComplexEntity;
import org.jdto.entities.SimpleAssociation;
import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.dtos.XMLTesterDTO;
import org.jdto.entities.SimpleEntity;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * This class contains copy/pasted tests from some of the features on the framework
 * just with the intention to know if the xml configuration builder is working fine
 * or not.
 * @author juancavallotti
 */
public class TestXMLMapping {

    static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder(TestXMLMapping.class.getResourceAsStream("/xmlmappingtest.xml"));
    }

    @Test
    public void testTransientBinding() {

        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setaString("this is my xml test");

        XMLTesterDTO dto = binder.bindFromBusinessObject(XMLTesterDTO.class, simpleEntity);

        //to test default bindng is working
        assertEquals(simpleEntity.getAnInt(), dto.getAnInt());
        //check transient
        assertNull("A string is transient so it should be null", dto.getaString());
    }

    @Test
    public void testSimpleBinding() {

        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setaString("this is my xml test");

        XMLTesterDTO dto = binder.bindFromBusinessObject(XMLTesterDTO.class, simpleEntity);

        //to test default bindng is working
        assertEquals(simpleEntity.getAnInt(), dto.getAnInt());
        //to test the source binding is working.
        assertEquals(simpleEntity.getaString(), dto.getDtoName());
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
    public void testComplexConverterDTOBinding() {
        SimpleEntity simpleBusinessObject = new SimpleEntity("simple", 123, 345.35222, true);

        String expectedPrice = String.format("$ %.2f", simpleBusinessObject.getaDouble());
        String expectedCompound = String.format("%.2f %08d", simpleBusinessObject.getaDouble(), simpleBusinessObject.getAnInt());


        FormatDTO dto = binder.bindFromBusinessObject(FormatDTO.class, simpleBusinessObject);

        assertNotNull("double format attribute should not be null", dto.getPrice());
        assertNotNull("compound format attribute should not be null", dto.getCompound());

        assertEquals("failed to get expected simple price", expectedPrice, dto.getPrice());
        assertEquals("failed to get expected compound price", expectedCompound, dto.getCompound());
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
    public void testImmutable() {
        SimpleEntity simpleBusinessObject = new SimpleEntity("simple", 123, 345.35, true);
        SimpleAssociation entity = new SimpleAssociation(simpleBusinessObject, "related");

        SimpleImmutableDTO dto = binder.bindFromBusinessObject(SimpleImmutableDTO.class, entity);

        //the other fields shouldnt be null
        assertEquals("second string is mapped with related", entity.getRelated().getaString(), dto.getSecondString());
        assertEquals("first string is mapped with myString", entity.getMyString(), dto.getFirstString());
    }
}
