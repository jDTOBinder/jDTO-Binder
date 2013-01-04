/*
 * Copyright 2013 Juan Alberto López Cavallotti.
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

package org.jdto.impl;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Value conversion helper tests.
 * @author Juan Alberto López Cavallotti
 */
public class TestValueConversionHelper {

    
    @Test
    public void testNullLongConversion() {
        
        long val = (Long) ValueConversionHelper.applyCompatibilityLogic(long.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val);
        
    }
    @Test
    public void testNullCharConversion() {
        
        char val = (Character) ValueConversionHelper.applyCompatibilityLogic(char.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val);
        
    }
    @Test
    public void testNullBooleanConversion() {
        
        boolean val = (Boolean) ValueConversionHelper.applyCompatibilityLogic(boolean.class, null);
        assertEquals("Null should be compatibilized to false", false, val);
        
    }
    @Test
    public void testNullIntConversion() {
        
        int val = (Integer) ValueConversionHelper.applyCompatibilityLogic(int.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val);
        
    }
    @Test
    public void testNullByteConversion() {
        
        byte val = (Byte) ValueConversionHelper.applyCompatibilityLogic(byte.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val);
        
    }
    @Test
    public void testNullDoubleConversion() {
        
        double val = (Double) ValueConversionHelper.applyCompatibilityLogic(double.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val, 0.0001);
        
    }
    
    @Test
    public void testNullFloatConversion() {
        
        float val = (Float) ValueConversionHelper.applyCompatibilityLogic(float.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val, 0.0001);
        
    }

    @Test
    public void testNullShortConversion() {
        
        short val = (Short) ValueConversionHelper.applyCompatibilityLogic(short.class, null);
        assertEquals("Null should be compatibilized to 0", 0, val);
        
    }
}
