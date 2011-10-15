package com.juancavallotti.jdto.impl.xml;

import org.apache.commons.lang.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * 
 * @author juancavallotti
 */
public class TestXMLMetadata {
    
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
        }
    }
    
}
