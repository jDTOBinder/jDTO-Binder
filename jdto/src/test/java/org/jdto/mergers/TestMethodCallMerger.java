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

import java.util.Arrays;
import java.util.List;
import org.jdto.dtos.UsefulEnum;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Formally test the method call merger.
 *
 * @author Juan Alberto López Cavallotti
 */
public class TestMethodCallMerger {

    /**
     * Object should work being stateless.
     */
    private static MethodCallMerger subject = new MethodCallMerger();

    @Test
    public void testCallCollectionSize() {

        List<String> testValue = Arrays.asList("First", "Second");
        Object expected = testValue.size();
        String[] method = {"size"};

        Object result = subject.mergeObjects(testValue, method);

        assertEquals("result should be the size of the collection.", expected, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNulls() {
        
        String[] method = {"getClass"};
        
        assertNull("Null object should return null", subject.mergeObjects(null, method));
        assertNull("Null object should return null", subject.restoreObject(null, method));
        
        //should throw illegal argument exception
        subject.mergeObjects(method, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testUnexistentMethod() {
        
        String[] method = {"nonSenseMethod"};
        //execute a non existing method.
        subject.mergeObjects(this, method);
    }
    
    @Test
    public void testCallEnumName() {

        UsefulEnum testValue = UsefulEnum.USEFUL;
        Object expected = testValue.name();
        String[] method = {"name"};
        
        assertFalse(subject.isRestoreSupported(method));
        
        Object result = subject.mergeObjects(testValue, method);

        assertEquals("Should be the name of the enum constant", expected, result);
    }

    @Test
    public void testConstructorUnmerge() {

        String[] params = {"notImportant", "java.lang.Integer"};
        assertTrue(subject.isRestoreSupported(params));
        
        String value = "10";
        
        Integer result = (Integer) subject.restoreObject(value, params);
        assertEquals("Should have the same value", 10, result.intValue());

    }
    
    @Test
    public void testStaticMethodCallUnmerge() {
        
        String[] params = {"notImportant", "java.lang.Integer", "parseInt"};
        assertTrue(subject.isRestoreSupported(params));
        
        String value = "12";
        Integer result = (Integer) subject.restoreObject(value, params);
        assertEquals("Should have the same value", 12, result.intValue());

    }
}
