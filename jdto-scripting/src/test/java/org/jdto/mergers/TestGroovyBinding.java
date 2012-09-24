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

package org.jdto.mergers;

import org.jdto.DTOBinder;
import org.jdto.DTOBinderFactory;
import org.jdto.mergers.dtos.GroovyBean;
import org.jdto.mergers.dtos.GroovyDTO;
import org.junit.Test;

import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding test for the groovy merger.
 * @author Juan Alberto López Cavallotti
 */
public class TestGroovyBinding {
    
    private static final Logger logger = LoggerFactory.getLogger(TestGroovyBinding.class);
    
    @Test
    public void testBinding() {
        
        //get the binder
        DTOBinder binder = DTOBinderFactory.getBinder();
        
        //build some test objects
        GroovyBean bean1 = new GroovyBean("Hello");
        GroovyBean bean2 = new GroovyBean("World");
        
        GroovyDTO dto = binder.bindFromBusinessObject(GroovyDTO.class, bean1, bean2);
        
        assertNotNull("DTO Should not be null", dto);
        
        assertNotNull("Single source attribute should be not null", dto.getSingleSource());
        
        logger.info("Single source value is: " + dto.getSingleSource());
        
        assertEquals("is not null", dto.getSingleSource());
        
        assertNotNull("Multi source attribute should be not null", dto.getMultipleSource());
        
        logger.info("Multiple source value is: " + dto.getMultipleSource());
        
        assertEquals("Hello and World", dto.getMultipleSource());
    }
    
}
