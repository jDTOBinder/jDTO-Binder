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

package org.jdto.util;

import org.apache.commons.lang3.exception.CloneFailedException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test classes extracted from commons Lang.
 * @author Juan Alberto López Cavallotti
 */
public class TestUtilityClasses {
    
    @Test
    public void testClassUtils() {
        
        //test the assignable
        boolean result = ClassUtils.isAssignable(getClass(), Object.class, true);
        
        //any class is assignable to object
        assertTrue(result);
        
        Class integer = ClassUtils.primitiveToWrapper(int.class);
        
        //should be the same
        assertEquals(Integer.class, integer);
    }
    
    
    @Test
    public void testObjectUtils() {
        
        Integer myInt = 45;
        
        Integer result = (Integer) ObjectUtils.clone(myInt);
        
        assertNull("Integers are not cloneable", result);
        
        class CloneFailure implements Cloneable {

            @Override
            protected Object clone() throws CloneNotSupportedException {
                throw new IllegalStateException();
            }
            
        }
        
        boolean producedException = false;
        try {
            ObjectUtils.clone(new CloneFailure());
        } catch (CloneFailedException ex) {
            producedException = true;
        }
        
        assertTrue(producedException);
    }
    
    
}
