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

import java.io.Serializable;
import org.jdto.DTOBinder;

/**
 * Manages the lifecycle of DTO Objects, this class is responsible for creating
 * a DTO binding context and dispatching it to objects that implement the 
 * appropriate lifecycle methods.
 * 
 * @author Juan Alberto López Cavallotti
 * @since 1.4
 */
class ObjectLifecycleManager implements Serializable {
    
    /**
     * The dto binder associated with this lifecycle manager.
     */
    private final DTOBinder binder;

    
    /**
     * Build a new instance of this lifecycle manager.
     * @param binder 
     */
    ObjectLifecycleManager(DTOBinder binder) {
        this.binder = binder;
    }
    
    /**
     * Notify the phase change to the target object by sending relevant binding information.
     * @param <T> The type of the target object.
     * @param phase the phase to notify.
     * @param target the target instance. 
     * @param metadata the bean metadata to pass to the target.
     * @param sourceBeans the source beans to pass to the objects.
     */
    <T> void notify(LifecyclePhase phase, T target, BeanMetadata metadata, Object... sourceBeans) {
        
    }
    
    
}
