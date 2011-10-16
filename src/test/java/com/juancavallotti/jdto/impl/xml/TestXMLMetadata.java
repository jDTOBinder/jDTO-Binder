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
