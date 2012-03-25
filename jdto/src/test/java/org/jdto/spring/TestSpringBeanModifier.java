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
package org.jdto.spring;

import org.jdto.spring.BeanWrapperBeanModifier;
import java.util.Map;
import java.util.HashMap;
import org.jdto.entities.ComplexEntity;
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

        modifier.writePropertyValue("association.related.aString", "lalala", testIt);

        //this should be not null because the modifier should have made up missing values on path.
        assertNotNull(testIt.getAssociation());
    }

    @Test
    public void testWriteToMap() {
        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();

        Map<String, String> instance = new HashMap();

        modifier.writePropertyValue("name", "myName", instance);

        //try to write simple property
        assertEquals("myName", instance.get("name"));

        //try to write nested property.
        modifier.writePropertyValue("name.last", "lastName", instance);

        assertEquals("lastName", instance.get("name.last"));
    }

    @Test
    public void testReadFromMap() {

        BeanWrapperBeanModifier modifier = new BeanWrapperBeanModifier();

        Map<String, String> instance = new HashMap<String, String>();

        instance.put("name", "myName");
        instance.put("name.last", "lastName");

        String simple = (String) modifier.readPropertyValue("name", instance);
        String compound = (String) modifier.readPropertyValue("name.last", instance);

        assertEquals("myName", simple);
        assertEquals("lastName", compound);
    }
}
