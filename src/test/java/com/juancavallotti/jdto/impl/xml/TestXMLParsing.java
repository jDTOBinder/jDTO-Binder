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

package com.juancavallotti.jdto.impl.xml;

import com.juancavallotti.jdto.impl.XMLBeanInspector;
import com.juancavallotti.jdto.dtos.XMLTesterDTO;
import java.util.HashMap;
import com.juancavallotti.jdto.impl.BeanMetadata;
import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * 
 * @author juancavallotti
 */
public class TestXMLParsing {
    
    static XMLBeanInspector inspector;
    
    @BeforeClass
    public static void initXMLFile() throws Exception {
        inspector = XMLBeanInspector.getInstance("/demoxml.xml");
    }
    
    @Test
    public void testXMLParsing() {
        assertNotNull(inspector);
        assertNotNull(inspector.getMappings());
        
        DTOMappings mappings = inspector.getMappings();
        
        assertFalse(mappings.getElements().isEmpty());
        
        for (DTOElement dTOElement : mappings.getElements()) {
            assertFalse(StringUtils.isEmpty(dTOElement.getType()));
            assertFalse(dTOElement.getBeanNames().isEmpty());
            for (String stringNames : dTOElement.getBeanNames()) {
                assertFalse(StringUtils.isEmpty(stringNames));
            }
            //check for the fields
            for (DTOTargetField dTOTargetField : dTOElement.getTargetFields()) {
                assertNotNull(dTOTargetField.getFieldName());
                assertNotNull(dTOTargetField.getFieldType());
                assertNotNull(dTOTargetField.getMerger());
                assertNotNull(dTOTargetField.getMergerParam());
                assertTrue(dTOTargetField.isCascade());
                assertTrue(dTOTargetField.isDtoTransient());
                assertFalse(dTOTargetField.getSources().isEmpty());
                for (DTOSourceField dTOSourceField : dTOTargetField.getSources()) {
                    assertNotNull(dTOSourceField.getMerger());
                    assertNotNull(dTOSourceField.getMergerParam());
                    assertNotNull(dTOSourceField.getName());
                    assertNotNull(dTOSourceField.getSourceBean());
                }
            }
        }
    }
    
    @Test
    public void testBuildMetadata() {
        HashMap<Class, BeanMetadata> metadata = inspector.buildMetadata();
        
        assertNotNull(metadata);
        
        assertTrue("resulting metadata should contain info about the configured bean", metadata.containsKey(XMLTesterDTO.class));
    }
    
}
