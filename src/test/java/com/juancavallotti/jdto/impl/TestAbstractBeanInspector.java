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
