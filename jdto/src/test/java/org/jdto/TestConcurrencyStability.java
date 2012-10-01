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
package org.jdto;

import java.util.ArrayList;
import java.util.List;
import org.jdto.entities.GeneralPurposeEntity;
import org.jdto.util.ConcurrencyRule;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test the framework produces expected results when used simultaneously by
 * multiple threads.
 * 
 * @author Juan Alberto López Cavallotti
 */
public class TestConcurrencyStability {
    
    private static final Logger logger = LoggerFactory.getLogger(TestConcurrencyStability.class);
    
    private static DTOBinder binder;
    
    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }
    
    /**
     * The test will run in parallell 100 times.
     */
    @Rule
    public ConcurrencyRule rule = new ConcurrencyRule(100);
    
    
    @Test
    public void testCase() {
        String threadName =  Thread.currentThread().getName();
        
        logger.debug(threadName);
        
        ArrayList<GeneralPurposeEntity> simpleEntities = new ArrayList<GeneralPurposeEntity>();
        
        //add 100 entities to the source collection
        for (int i = 0; i < 100; i++) {
            simpleEntities.add(new GeneralPurposeEntity(threadName+" "+i, null, null, 0.0, null, i));
        }
        
        List<GeneralPurposeEntity> dtos = binder.bindFromBusinessObjectList(GeneralPurposeEntity.class, simpleEntities);
        
        assertEquals("Lists should have the same size", simpleEntities.size(), dtos.size());
        
        //check every relevant field
        for (int i = 0; i < simpleEntities.size(); i++) {
            GeneralPurposeEntity source = simpleEntities.get(i);
            GeneralPurposeEntity target = dtos.get(i);
            assertNotSame("Objects should not be the same", source, target);
            assertEquals("The String should be equal",source.getTheString(), target.getTheString());
            assertEquals("The int should be equals", source.getTheInt(), target.getTheInt());
        }
    }
}
