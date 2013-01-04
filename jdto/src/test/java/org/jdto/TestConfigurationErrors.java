/*
 * Copyright 2012 Juan Alberto López Cavallotti.
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
package org.jdto;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdto.impl.DTOBinderBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test different scenarios where configuration errors can happen.
 * @author Juan Alberto López Cavallotti
 */
public class TestConfigurationErrors {
    
    
    @BeforeClass
    public static void globalInit() {
        Logger logger = Logger.getLogger("org.jdto.impl.BeanClassUtils");
        logger.setLevel(Level.OFF);
        logger = Logger.getLogger("org.jdto.impl.AbstractBeanInspector");
        logger.setLevel(Level.OFF);
    }
    
    @AfterClass
    public static void globalTearDown() {
        Logger logger = Logger.getLogger("org.jdto.impl.BeanClassUtils");
        logger.setLevel(Level.INFO);
        logger = Logger.getLogger("org.jdto.impl.AbstractBeanInspector");
        logger.setLevel(Level.INFO);
    }
    
    /**
     * Check the cases when the provided xml resoruce is null
     */
    @Test(expected=IllegalArgumentException.class)
    public void testNullXMLConfiguration() {
        
        DTOBinderFactory.buildBinder((InputStream)null);
        
        fail("Should not have reached this point");
    }
    
    
    /**
     * Excercises the handler for unparseable xml configuration resource.
     */
    @Test(expected=RuntimeException.class)
    public void testUnparseableResource() {
        
        DTOBinderFactory.buildBinder(getClass().getResourceAsStream("/erroneous/unparseable.xml"));
        
        fail("Should have not reached this point");
    }
    
    
    
    /**
     * Test the exception when an invalid bean class is defined in the XML File.
     */
    @Test
    public void testInvalidBeanClass() {
        
        //build the configuration.
        DTOBinderBean bean = (DTOBinderBean) DTOBinderFactory.buildBinder(getClass().getResourceAsStream("/erroneous/unexistent.xml"));
        
        //test that the metadata is empty.
        assertTrue("Metadata should be empty.", bean.getMetadata().isEmpty());
    }
    
}
