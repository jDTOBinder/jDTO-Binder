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

package com.juancavallotti.jdto;

import java.util.List;
import com.juancavallotti.jdto.dtos.FormatDTO;
import com.juancavallotti.jdto.entities.SimpleEntity;
import java.util.LinkedList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class TestBindCollection {

    private static DTOBinder binder;

    @BeforeClass
    public static void globalInit() {
        binder = DTOBinderFactory.buildBinder();
    }

    @Test
    public void testBindSimpleCollection() {
        LinkedList<SimpleEntity> simpleEntities = new LinkedList<SimpleEntity>();
        simpleEntities.add(new SimpleEntity("simple 1", 12, 45.56, true));
        simpleEntities.add(new SimpleEntity("simple 2", 34, 56.67, false));
        
        List<FormatDTO> dtos = binder.bindFromBusinessObjectList(FormatDTO.class, simpleEntities);
        
        assertEquals("both lists should have the same size", simpleEntities.size(), dtos.size());
    }
}
