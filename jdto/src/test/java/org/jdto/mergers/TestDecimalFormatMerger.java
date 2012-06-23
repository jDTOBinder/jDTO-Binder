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
    public void testDecimalFormatMerger() {
        
        double value = 0.101;
        
        String result = subject.mergeObjects(value, new String[] {"% #,###.00"});
        
        assertEquals(new DecimalFormat("% #,###.00").format(value), result);
    }
    
    public void testDecimalFormatUnmerge() {
        String number = "12.5";
        
        String[] params = {"#,###.00"};
        
        assertTrue(subject.isRestoreSupported(params));
        
        Number result = subject.restoreObject(number, params);
        
        assertEquals("Should have found the same number but as double.", 12.5, result.doubleValue(), 0.00001);
    }
}
