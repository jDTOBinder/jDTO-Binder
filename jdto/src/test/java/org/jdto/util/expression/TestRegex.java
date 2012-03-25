/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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

package org.jdto.util.expression;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the regex used to distinguish variables from numbers. This does not test
 * real code but is useful during development, and stored for future usage.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestRegex {
    private static final String variableRegex = "[A-Za-z]{1}+[.[A-Za-z]]*";
    
    
    @Test
    public void testMatchesSimple() {
        
        String value = "varA";
        String reallySimple = "b";
        
        assertTrue(reallySimple.matches(variableRegex));
        assertTrue(value.matches(variableRegex));
    }
    
    @Test
    public void testMatchesCompound() {
        
        String largeProps = "varA.abc";
        String simpleProps = "b.c.d";
        
        assertTrue(simpleProps.matches(variableRegex));
        assertTrue(largeProps.matches(variableRegex));
    }
    
    @Test
    public void testNotMatchesNumeric() {
        
        String positive = "20";
        String negative = "-20";
        String decimalNegative = "-20.30";
        String decimalPositive = "2033.44";
        
        assertFalse(positive.matches(variableRegex));
        assertFalse(negative.matches(variableRegex));
        assertFalse(decimalNegative.matches(variableRegex));
        assertFalse(decimalPositive.matches(variableRegex));
        
        
    }
}
