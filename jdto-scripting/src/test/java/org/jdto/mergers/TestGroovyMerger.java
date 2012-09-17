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

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Check the sanity of the groovy merger.
 * 
 * @author Juan Alberto López Cavallotti
 */
public class TestGroovyMerger {
    
    private GroovyMerger groovyMerger;
    
    @Before
    public void initTest() {
        groovyMerger = new GroovyMerger();
    }
    
    
    @Test
    public void testToString() {
                
        Integer myInt = 20;
        
        String[] script = { "sourceValue.toString()" };
        
        String result = (String) groovyMerger.mergeObjects(myInt, script);
        
        assertEquals("Should be equals: ", myInt.toString(), result);
        
    }
    
    @Test
    public void testNullChecks() {
        
        String[] script = { " sourceValue == null ? 'isNull' : 'isNotNull' " };
        
        String result = (String) groovyMerger.mergeObjects(null, script);
        
        assertEquals("Should say 'isNull' ", "isNull", result);
    }
    
    @Test
    public void testCommons() {
        
        String[] script = {"StringUtils.reverse(sourceValue)"};
        
        String object = "start";
        
        String result = (String) groovyMerger.mergeObjects(object, script);
        
        assertEquals("Should be the reverse of the string", StringUtils.reverse(object), result);
        
    }
    
    @Test
    public void testLogging() {
        
        String[] script = {"logger.error('Value is: ' + sourceValue); return sourceValue;"};
        
        String original = "TEST";
        
        String result = (String) groovyMerger.mergeObjects(original, script);
        
        assertEquals("Object should remain unchanged.", original, result);
    }
    
}
