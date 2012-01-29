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

import com.juancavallotti.jdto.BeanModifier;
import com.juancavallotti.jdto.BeanModifierAware;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import com.juancavallotti.jdto.impl.BeanClassUtils;
import java.util.Collection;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge a Collection of complex objects into a Collection of a property given 
 * by the extra param. <br />
 * The returned collection is of the same type of the original collection, and 
 * the following rules will apply to create the returned collection: <br />
 * <ol>
 *  <li>If the collection is cloneable, then {@link java.lang.Object#clone() clone()} 
 * will be called and then {@link java.util.Collection#clear() clear()} to empty the collection.</li>
 *  <li>If the collection is not cloneable, then try to create a new instance of its same class.</li>
 * </ol>
 * 
 * @author juancavallotti
 */
public class PropertyCollectionMerger implements SinglePropertyValueMerger<Collection, Collection>, BeanModifierAware {

    private static final Logger logger = LoggerFactory.getLogger(PropertyCollectionMerger.class);

    @Override
    public Collection mergeObjects(Collection values, String[] extraParams) {

        //this should be null safe
        if (values == null) {
            return null;
        }
        
        if (ArrayUtils.isEmpty(extraParams)) {
            throw new IllegalArgumentException("You need to provide the extra param should be a property name.");
        }
        
        String extraParam = extraParams[0];
        
        Collection retValue = getCollectionToReturn(values);
        
        if (retValue == null) {
            logger.warn("Could not create proper collection of type: "+values.getClass().getName());
            return null;
        }
        
        //traverse the collection
        for (Object value : values) {
            Object propertyValue = modifier.readPropertyValue(extraParam, value);
            retValue.add(propertyValue);
        }
        
        return retValue;
    }

    private Collection getCollectionToReturn(Collection value) {
        try {
            Collection ret = null;

            if (value instanceof Cloneable) {
                ret = (Collection) ObjectUtils.clone(value);
                ret.clear();
            } else {
                //try to create a new instance of the collection
                ret = BeanClassUtils.createInstance(value.getClass());
            }
            return ret;
        } catch (Exception ex) {
            logger.error("Exception while trying to instantiate collection", ex);
            throw new RuntimeException(ex);
        }
    }
    private BeanModifier modifier;

    @Override
    public void setBeanModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }
}
