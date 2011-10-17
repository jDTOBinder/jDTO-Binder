package com.juancavallotti.jdto.impl.xml;

import com.juancavallotti.jdto.DTOBinder;
import com.juancavallotti.jdto.DTOBinderFactory;
import com.juancavallotti.jdto.dtos.XMLTesterDTO;
import com.juancavallotti.jdto.entities.SimpleEntity;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;
/**
 *
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
    
    
    
}
