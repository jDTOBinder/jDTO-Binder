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

package com.juancavallotti.jdto.spring;

import com.juancavallotti.jdto.entities.ComplexEntity;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestSpringBeanModifier {
    
    @Test
    public void testNullPath() {
        ComplexEntity testIt = new ComplexEntity();
        
        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();
        
        Object value = modifier.readPropertyValue("association.related", testIt);
        
        assertNull(value);
    }
    
    @Test
    public void testSetNullPath() {
        
        ComplexEntity testIt = new ComplexEntity();
        
        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();
        
        modifier.writePropertyValue("association.related", "lalala", testIt);
        
        assertNull(testIt.getAssociation());
    }
}
