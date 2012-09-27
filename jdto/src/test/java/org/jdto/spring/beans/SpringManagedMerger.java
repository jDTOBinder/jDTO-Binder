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
package org.jdto.spring.beans;

import javax.annotation.PostConstruct;
import org.jdto.SinglePropertyValueMerger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test spring managed merger.
 * @author Juan Alberto López Cavallotti
 */
public class SpringManagedMerger implements SinglePropertyValueMerger<Boolean, Object> {

    @Autowired
    private SpringIntegrationComponent myComponent;
    
    @PostConstruct
    private void init() {
        myComponent.testIt();
    }
    
    @Override
    public Boolean mergeObjects(Object value, String[] extraParam) {
        return myComponent.isPassed();
    }

    @Override
    public boolean isRestoreSupported(String[] params) {
        return false;
    }

    @Override
    public Object restoreObject(Boolean object, String[] params) {
        return null;
    }
    
}
