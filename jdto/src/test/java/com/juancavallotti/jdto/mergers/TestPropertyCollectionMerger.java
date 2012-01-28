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
package com.juancavallotti.jdto.mergers;

import com.juancavallotti.jdto.entities.SimpleEntity;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import java.util.LinkedList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;
/**
 * Acceptance tests for the property collection merger.
 * @author juancavallotti
 */
public class TestPropertyCollectionMerger {

    private static PropertyCollectionMerger merger;
    
    @BeforeClass
    public static void globalInit() {
        CoreBeanModifier modifier = new CoreBeanModifier();
        merger = new PropertyCollectionMerger();
        merger.setBeanModifier(modifier);
    }

    @Test
    public void testPropertyCollectionMerger() {
        List<SimpleEntity> originalCollection = new LinkedList<SimpleEntity>();

        originalCollection.add(new SimpleEntity("test", 10, 11.1, true));
        originalCollection.add(new SimpleEntity("test2", 10, 11.1, true));

        List<String> result = (List<String>) merger.mergeObjects(originalCollection, "aString");
        
        assertEquals(originalCollection.get(0).getaString(), result.get(0));
        assertEquals(originalCollection.get(0).getaString(), result.get(0));
    }
}
