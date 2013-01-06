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

package org.jdto.dtos;

import java.io.Serializable;
import org.jdto.DTOBindingContext;
import org.jdto.annotation.Source;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * DTO To test binding lifecycle.
 * @author Juan Alberto López Cavallotti
 */
public class LifecycleDTO implements Serializable {
    
    private String stringProperty;
    
    //usage statistics
    private boolean beforeCalled;
    private boolean afterCalled;
    private long beforeTimestamp;
    private long afterTimestamp;
    
    private static final Logger logger = LoggerFactory.getLogger(LifecycleDTO.class);
    
    public void beforePropertiesSet(DTOBindingContext context) {
        
        logger.info("Before properties set called");
        beforeCalled = true;
        beforeTimestamp = System.currentTimeMillis();
        assertFalse("After called before before?", afterCalled);
        assertNull(getStringProperty());
    }
    
    public void afterPropertiesSet(DTOBindingContext context) {
        logger.info("After properties set called");
        afterCalled = true;
        afterTimestamp = System.currentTimeMillis();
        
        assertTrue("Before called after after?", beforeCalled);
        assertNotNull(getStringProperty());
    }
    
    
    public boolean beforeHasBeenCalled() {
        return beforeCalled;
    }
    
    public boolean afterHasBeenCalled() {
        return afterCalled;
    }
    
    public long beforeCallTime() {
        return beforeTimestamp;
    }
    
    public long afterCallTime() {
        return afterTimestamp;
    }
    
    //getters and setters
    
    @Source("aString")
    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }
    
}
