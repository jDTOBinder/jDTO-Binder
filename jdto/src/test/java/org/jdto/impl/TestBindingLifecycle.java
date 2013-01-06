/*
 * Copyright 2013 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.DTOBindingContext;
import org.jdto.dtos.LifecycleDTO;
import org.jdto.entities.SimpleEntity;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class to verify lifecycle health.
 * @author Juan Alberto López Cavallotti
 */
public class TestBindingLifecycle {
    
    private static DTOBinder binder;
    
    private static Level currentLevel;
    
    @BeforeClass
    public static void globalInit() {
        
        //increase logging level
        currentLevel = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getLevel();
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST);
        
        binder = DTOBinderFactory.buildBinder();
    }
    
    
    @Test
    public void testBindingLifecycle() {
        
        //build the source value.
        SimpleEntity source = new SimpleEntity("MyString", 0, 0.0, true);
        
        LifecycleDTO dto = binder.bindFromBusinessObject(LifecycleDTO.class, source);
        
        //check lifecycle invocation.
        assertTrue("Before lifecycle not invoked", dto.beforeHasBeenCalled());
        assertTrue("After lifecycle not invoked", dto.afterHasBeenCalled());
        assertTrue("Before should have lower or equals timestamp than after", dto.beforeCallTime() <= dto.afterCallTime());
    }
    
    @Test
    public void testPrivateLifecycle() {
        
        //build the source value.
        SimpleEntity source = new SimpleEntity("MyString", 0, 0.0, true);
        PrivateLifecycle dto = binder.bindFromBusinessObject(PrivateLifecycle.class, source);
        
        assertEquals(source.getaString(), dto.getaString());
        assertFalse("Lifecycle called on private method", dto.lifecycleCalled());
    }

    @Test
    public void testExceptionLifecycle() {
        
        //build the source value.
        SimpleEntity source = new SimpleEntity("MyString", 0, 0.0, true);
        ExceptionLifecycle dto = binder.bindFromBusinessObject(ExceptionLifecycle.class, source);
        
        assertEquals(source.getaString(), dto.getaString());
    }
    
    
    @AfterClass
    public static void globalTearDown() {        
        //rollback the logger to its original state.
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(currentLevel);
    }
    
    
    //internal class to check different error conditions.
    public static class PrivateLifecycle {
        
        private String aString;
        private boolean called;
        
        public String getaString() {
            return aString;
        }

        public void setaString(String aString) {
            this.aString = aString;
        }
        
        
        private void afterPropertiesSet(DTOBindingContext context) {
            called = true;
        }
        
        public boolean lifecycleCalled() {
            return called;
        }
        
    }
    
    //internal class to check different error conditions.
    public static class ExceptionLifecycle {
        
        private String aString;

        public String getaString() {
            return aString;
        }

        public void setaString(String aString) {
            this.aString = aString;
        }
        
        
        public void afterPropertiesSet(DTOBindingContext context) {
            throw new IllegalArgumentException("Testing exception");
        }
    }
    
}
