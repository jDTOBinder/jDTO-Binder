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

import java.util.ArrayList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for some basic object operations.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestSimpleObjectMergers {
    
    @Test
    public void testToStringMerger() {
        ToStringMerger merger = new ToStringMerger();

        String result = null;
        
        //testing nulls
        result = merger.mergeObjects(null, null);
        
        assertNull("Result should be null", result);
        
        //testing strings
        String testString = "test string";
        result = merger.mergeObjects(testString, null);
        assertEquals("Strings should be equals",testString, result);
        
        //testing primitives
        result = merger.mergeObjects(10, null);
        assertEquals("Should be equals to 10","10", result);
    }
    
    @Test
    public void testCloneMerger() {
        CloneMerger merger = new CloneMerger();

        Object result = null;
        
        //testing nulls
        result = merger.mergeObjects(null, null);
        
        assertNull("Clone result should be null", result);
        
        //testing strings that are not cloneable
        String testString = "test string";
        
        result = merger.mergeObjects(testString, null);
        assertNull("Strings are not cloneable!",result);
        
        ArrayList list = new ArrayList();
        
        //testing primitives
        result = merger.mergeObjects(list, null);
        assertEquals("Lists should be equals",list, result);
        assertNotSame("Lists should not be the same",testString, result);
    }
    
    @Test
    public void testCloneFailed() {
        
        CloneMerger merger = new CloneMerger();
        
        //restore is not supported for this merger.
        assertFalse(merger.isRestoreSupported(null));
        assertNull(merger.restoreObject(null, null));
        
        class NotCloneable {
            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException();
            }
        }
        
        Object result = merger.mergeObjects(new NotCloneable(), null);
        
        assertNull("Object was not cloneable", result);
    }
    
    enum TestLiterals {
        MyLiteral{

            @Override
            public String toString() {
                return "Something ELSE!";
            }
        };
    }
    
    
    @Test
    public void testEnumMerger() {
        EnumMerger merger = new EnumMerger();

        String result = null;
        
        //this merger is not supposed to restore this kinds of values, because
        //the functionality is already built in.
        assertFalse(merger.isRestoreSupported(null));
        assertNull(merger.restoreObject(null, null));
        
        
        //testing nulls
        result = merger.mergeObjects(null, null);
        
        assertNull("String representation result should be null", result);
        
        
        //test getting the name of the enum
        result = merger.mergeObjects(TestLiterals.MyLiteral, null);
        assertEquals("Thould be the same as calling name() on the enum literal", TestLiterals.MyLiteral.name(), result);
        
    }
    
}
