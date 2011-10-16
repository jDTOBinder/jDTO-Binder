package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.entities.SimpleEntity;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
/**
 * Test if the base bean inspector is working or not.
 * @author juancavallotti
 */
public class TestAbstractBeanInspector extends AbstractBeanInspector{
    
    List<String> discoveredProperties;
    
    @Override
    FieldMetadata buildFieldMetadata(String propertyName, Method readAccessor, Class beanClass) {
        FieldMetadata defaultFieldMetadata = super.buildDefaultFieldMetadata(propertyName);
        
        discoveredProperties.add(propertyName);
        
        return defaultFieldMetadata;
    }

    @Override
    String[] readSourceBeanNames(Class beanClass) {
        return super.defaultSourceBeanNames();
    }
    
    @Before
    public void initTestCase() {
        discoveredProperties = new ArrayList<String>();
    }
    
    @Test
    public void testPropertyDiscovery() {
        //the test case
        inspectBean(SimpleEntity.class);
        
        assertTrue(discoveredProperties.contains("aString"));
        assertTrue(discoveredProperties.contains("anInt"));
        assertTrue(discoveredProperties.contains("aDouble"));
        assertTrue(discoveredProperties.contains("aBoolean"));
    }
    
}
