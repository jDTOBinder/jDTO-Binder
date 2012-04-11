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
package org.jdto.impl;

import java.lang.reflect.Field;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test methods for the BeanPropertyUtils class.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestBeanPropertyUtils {
    
    @Test
    public void testReadSafeField() {
        
        String testFieldName = "myField";        
        
        Field field = BeanPropertyUtils.readSafeField(TBPUB.class, testFieldName);
        
        assertNotNull("Inherited field should be found anyway.", field);
    }
    
}

/**
 * Sample class to test inheritance
 * @author Juan Alberto Lopez Cavallotti
 */
class TBPUA {
    private String myField;
    
    public String getMyField() {
        return myField;
    }

    public void setMyField(String myField) {
        this.myField = myField;
    }
    
}

/**
 * Sample class to test inheritance
 * @author Juan Alberto Lopez Cavallotti
 */
class TBPUB extends TBPUA {

}