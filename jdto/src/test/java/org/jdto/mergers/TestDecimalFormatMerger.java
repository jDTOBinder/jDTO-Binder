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

import java.text.DecimalFormat;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestDecimalFormatMerger {
    
    private static DecimalFormatMerger subject;
    
    @BeforeClass
    public static void init() {
        subject = new DecimalFormatMerger();
    }

    @Test
    public void testNullFormat() {
        
        final String testFormat = "#,###.00";
        
        String[] params = {testFormat};
        Object result = subject.mergeObjects(null, params);
        
        assertNull("Merge null should produce null", result);
        
        result = subject.restoreObject(null, params);
        assertNull("Restore null should produce null", result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testEmptyParams() {
        
        String[] params = {};
        subject.mergeObjects(12.34, params);
        fail("Should not have reached this point");
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testIllegalFormatStringRestore() {
        
        final String testFormat = "#,###.00";
        
        String[] params = {testFormat};
        subject.restoreObject("# abcd # efghi", params);
        fail("Should not have reached this point");
        
    }
        
    @Test
    public void testDecimalFormatMerger() {
        
        double value = 0.101;
        final String testFormat = "% #,###.00";
        
        String result = subject.mergeObjects(value, new String[] {testFormat});
        
        assertEquals(new DecimalFormat(testFormat).format(value), result);
    }
    
    @Test
    public void testDecimalFormatRestore() throws Exception {
        String number = "12,5";

        final String testFormat = "#,###.00";
        
        String[] params = {testFormat};
        
        assertTrue(subject.isRestoreSupported(params));
        
        Number result = subject.restoreObject(number, params);
        
        double expected = new DecimalFormat(testFormat).parse(number).doubleValue();
        
        assertEquals("Should have found the same number but as double.", expected, result.doubleValue(), 0.00001);
    }
}
