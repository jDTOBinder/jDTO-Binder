/*
 * Copyright 2014 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.mergers;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test the JSR 233 Srcipting Merger.
 * @author Juan Alberto López Cavallotti
 */
public class TestScriptingMerger {
    private ScriptMerger merger;
    
    @Before
    public void initTests() {
        merger = new ScriptMerger();
    }
    
    @Test
    public void testJavascriptMerging() {
        
        //simple javacript script.
        String script = " function a() { return 'Hello ' + sourceValue } ; a()";
        String[] params = { script };
        
        Object result = merger.mergeObjects("John", params);
        
        assertEquals("Should be 'Hello John'", "Hello John", result);
    }
    
    @Test
    public void testGenericGroovy() {
        
        //use generic support for executing groovy scripts.
        String script = "[a: 'test', b: sourceValue]";
        String[] params = { script , "groovy" };
        final String testValue = "Something";
        
        Object result = merger.mergeObjects(testValue, params);
        
        assertTrue("Result should be a map", result instanceof Map);
        
        Map<String, String> resultMap = (Map<String, String>) result;
        
        assertThat(resultMap, allOf(hasEntry("a", "test"), hasEntry("b", testValue)));
    }
    
}
