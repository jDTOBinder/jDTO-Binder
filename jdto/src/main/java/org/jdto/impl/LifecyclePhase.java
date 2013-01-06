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
package org.jdto.impl;

/**
 * List of lifecycle phases interesting for DTO Binding.
 * 
 * @author Juan Alberto López Cavallotti
 * @since 1.4
 */
enum LifecyclePhase {
    
    /**
     * This lifecycle phase happens after the target DTO is created but before
     * start setting its properties.
     */
    BEFORE_PROPERTIES_SET("beforePropertiesSet"),
    
    /**
     * This lifecycle phase happens after the target DTO is populated and before
     * it is returned.
     */
    AFTER_PROFPERTIES_SET("afterPropertiesSet"),;
    
    
    private final String handlerMethodName;

    private LifecyclePhase(String handlerMethodName) {
        this.handlerMethodName = handlerMethodName;
    }

    /**
     * The name of the conventional handler method for this lifecycle phase.
     * @return a String with the method name.
     */
    public String getHandlerMethodName() {
        return handlerMethodName;
    }
    
}
