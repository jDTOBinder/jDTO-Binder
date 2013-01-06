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

package org.jdto;

import java.io.Serializable;

/**
 * Contains useful information that can be used when taking advantage of the 
 * DTO Binding lifecycle.
 * 
 * @author Juan Alberto López Cavallotti
 * @since 1.4
 */
public final class DTOBindingContext implements Serializable {

    private final DTOMetadata metadata;
    private final DTOBinder binder;
    private final Object[] sourceObjects;
    
    /**
     * Build a new instance of the binding context.
     * @param metadata See {@link #getMetadata() } for more information.
     * @param binder See {@link #getBinder() } for more information.
     * @param sourceObjects  See {@link #getSourceObjects() } for more information.
     */
    public DTOBindingContext(DTOMetadata metadata, DTOBinder binder, Object[] sourceObjects) {
        this.metadata = metadata;
        this.binder = binder;
        this.sourceObjects = sourceObjects;
    }
    
    /**
     * Framework information for the bean that is currently on the procees of
     * being populated.
     * 
     * @return a DTOMetadata instance information of the bean being populated. 
     */
    public DTOMetadata getMetadata() {
        return metadata;
    }
    
    /**
     * DTOBinder instance being used to populate objects.
     * @return an implementation of DTOBinder.
     */
    public DTOBinder getBinder() {
        return binder;
    }
    
    /**
     * The source objects used to populate the current bean.
     * 
     * @return the objects as passed to {@link DTOBinder#bindFromBusinessObject(java.lang.Class, java.lang.Object[]) }.
     */
    public Object[] getSourceObjects() {
        return sourceObjects;
    }
    
}
