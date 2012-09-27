/*
 * Copyright 2012 Juan Alberto López Cavallotti.
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
package org.jdto.spring;

import org.jdto.DTOBinder;
import org.jdto.mergers.ToStringMerger;
import org.jdto.spring.beans.SpringManagedMerger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Test the integration with the spring framework.
 * @author Juan Alberto López Cavallotti
 */
@ContextConfiguration(locations="classpath:/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSpringIntegration {
    
    @Autowired
    private DTOBinder dtoBinder;
    
    @Autowired
    private ToStringMerger springMerger;
        
    @Test
    public void testSpringMergers() {
        
        ToStringMerger myMerger = dtoBinder.getPropertyValueMerger(ToStringMerger.class);
        
        assertSame("Mergers should be the same", springMerger, myMerger);
        
    }
    
    @Test
    public void testAutowiredMergers() {
        
        SpringManagedMerger myMerger = dtoBinder.getPropertyValueMerger(SpringManagedMerger.class);
        
        //nothing to merge :)
        assertTrue("Merget should pass the condition", myMerger.mergeObjects(null, null));
    }
}
