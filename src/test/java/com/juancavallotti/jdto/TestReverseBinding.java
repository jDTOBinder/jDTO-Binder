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
