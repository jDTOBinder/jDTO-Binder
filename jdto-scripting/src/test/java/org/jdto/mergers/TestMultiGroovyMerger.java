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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Check the sanity of the multi groovy merger.
 * @author Juan Alberto López Cavallotti
 */
public class TestMultiGroovyMerger {
    
    private MultiGroovyMerger groovyMerger;
    
    @Before
    public void init() {
        groovyMerger = new MultiGroovyMerger();
    }
    
    @Test
    public void testMultiple() {
        
        String[] script = {" sourceValues[0] + sourceValues[1] "};
        
        List values = Arrays.asList(20.2, 9.8);
        
        Double expected = 30.0;
        
        Double result = (Double) groovyMerger.mergeObjects(values, script);
        
        assertEquals("Should be the same result", expected, result, 0.0001);
    }
    
}
