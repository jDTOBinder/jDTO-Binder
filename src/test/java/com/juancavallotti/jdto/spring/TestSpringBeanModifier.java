package com.juancavallotti.jdto.spring;

import com.juancavallotti.jdto.entities.ComplexEntity;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestSpringBeanModifier {
    
    @Test
    public void testNullPath() {
        ComplexEntity testIt = new ComplexEntity();
        
        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();
        
        Object value = modifier.readPropertyValue("association.related", testIt);
        
        assertNull(value);
    }
    
    @Test
    public void testSetNullPath() {
        
        ComplexEntity testIt = new ComplexEntity();
        
        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();
        
        modifier.writePropertyValue("association.related", "lalala", testIt);
        
        assertNull(testIt.getAssociation());
    }
}
