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

package org.jdto.impl;

import org.jdto.impl.BeanClassUtils;
import org.jdto.dtos.SimpleAssociationDTO;
import org.jdto.dtos.SimpleImmutableDTO;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestBeanClassUtils {
    
    @Test
    public void testHasDefaultConstructor() {
        boolean shouldBeTrue = BeanClassUtils.hasDefaultConstructor(SimpleAssociationDTO.class);
        
        assertTrue("The class SimpleAssociationDTO has a default constructor", shouldBeTrue);
        
        boolean shouldBeFalse = BeanClassUtils.hasDefaultConstructor(SimpleImmutableDTO.class);
        
        assertFalse("The class SimpleImmutableDTO does not have a default constructor", shouldBeFalse);
    }
}
