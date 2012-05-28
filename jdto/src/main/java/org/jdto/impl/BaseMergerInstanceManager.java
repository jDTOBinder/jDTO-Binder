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

import java.io.Serializable;
import org.jdto.BeanModifier;
import org.jdto.BeanModifierAware;
import org.jdto.PropertyValueMerger;
import org.jdto.PropertyValueMergerInstanceManager;

/**
 * Base implementation of the property value merger instance manager. <br /><br />
 * 
 * jDTO Binder INTERNAL API - DO NOT USE!! <br /><br />
 * 
 * This implementation injects framework dependencies via the standard setter 
 * injection method and it uses the internal instance pool to cache the mergers it
 * creates.
 * 
 * @since 1.2
 * @author Juan Alberto Lopez Cavallotti
 */
public class BaseMergerInstanceManager implements PropertyValueMergerInstanceManager, Serializable {
    private static final long serialVersionUID = 1L;
    
    private BeanModifier modifier;

    @Override
    public <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass) {
        T merger = InstancePool.getOrCreate(mergerClass);
        injectContextIfNeeded(merger);
        return merger;
    }
    
    
    private void injectContextIfNeeded(Object target) {

        if (target instanceof BeanModifierAware) {
            BeanModifierAware bma = (BeanModifierAware) target;
            bma.setBeanModifier(modifier);
        }

    }

    public BeanModifier getModifier() {
        return modifier;
    }

    public void setModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }
}
