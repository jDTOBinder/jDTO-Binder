package com.juancavallotti.jdto.impl;

import java.util.ArrayList;
import com.juancavallotti.jdto.entities.ComplexList;
import com.juancavallotti.jdto.entities.ComplexEntity;
import com.juancavallotti.jdto.entities.SimpleAssociation;
import com.juancavallotti.jdto.entities.SimpleEntity;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestCoreBeanModifier {
    
    ComplexEntity testIt;
    ComplexList complexList;
    
    @Before
    public void initTestEntity() {
        
        SimpleEntity related = new SimpleEntity("mystring", 30, 40, true);
        
        SimpleAssociation assoc = new SimpleAssociation(related, "relatedString");
        
        testIt = new ComplexEntity("testName", assoc, 20);
        
        complexList = new ComplexList(new ArrayList(Arrays.asList(related)));
    }
    
    @Test
    public void testReadPropertyPath() {
        
        CoreBeanModifier modifier = new CoreBeanModifier();

        SimpleEntity entity = (SimpleEntity) modifier.readPropertyValue("association.related", testIt);

        assertNotNull(entity);
        
        String stringProp = (String) modifier.readPropertyValue("association.related.aString", testIt);
        
        assertEquals(testIt.getAssociation().getRelated().getaString(), stringProp);
        
        boolean booleanProp = (Boolean) modifier.readPropertyValue("association.related.aBoolean", testIt);
        
        assertEquals(testIt.getAssociation().getRelated().isaBoolean(), booleanProp);
        
        String firstAttrString = (String) modifier.readPropertyValue("name", testIt);
        
        assertEquals(testIt.getName(), firstAttrString);
    }
    
    @Test
    public void testWritePropertyPath() {
        
        CoreBeanModifier modifier = new CoreBeanModifier();
        
        modifier.writePropertyValue("name", "myName", testIt);
        
        assertEquals("myName", testIt.getName());
        
        modifier.writePropertyValue("association.myString", "woohoo", testIt);
        
        assertEquals("woohoo", testIt.getAssociation().getMyString());
        
        modifier.writePropertyValue("association.related.aString", "woohoo", testIt);
        
        assertEquals("woohoo", testIt.getAssociation().getRelated().getaString());
        
        modifier.writePropertyValue("association.related.aBoolean", false, testIt);
        
        assertFalse(testIt.getAssociation().getRelated().isaBoolean());
    }
    
    @Test
    public void testReadCollections() {
        
        CoreBeanModifier modifier = new CoreBeanModifier();
        
        ArrayList readValue = (ArrayList) modifier.readPropertyValue("sourceList", complexList);
        
        assertSame(complexList.getSourceList(), readValue);
    }
}
