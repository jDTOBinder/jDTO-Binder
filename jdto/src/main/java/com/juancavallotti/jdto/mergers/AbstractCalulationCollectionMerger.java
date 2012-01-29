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
import com.juancavallotti.jdto.impl.util.UniversalIterable;
import org.apache.commons.lang.StringUtils;

/**
 * Base class to implement a merger based on looping over a collection or array.
 * Which will produce a result based on a calculation.
 * <br />
 * 
 * This class will loop over the collection and let the imlementation decide
 * what to do with the resulting value which must be a Double.
 * @author juancavallotti
 */
public abstract class AbstractCalulationCollectionMerger implements SinglePropertyValueMerger<Double, Object>, BeanModifierAware {

    @Override
    public Double mergeObjects(Object input, String[] extraParam) {
        //the value could be null so check that.
        if (input == null) {
            return 0.0;
        }

        //convert it into an iterable instance.
        Iterable values = convertInput(input);

        return processCalculation(values, extraParam);
    }

    /**
     * Concrete implementation of the calculation processing cycle.
     * @param collection the input pre-processed as a collection so it can be iteraded on a foreach loop.
     * @param extraParam the merge extra-param.
     * @return the result of the calculation.
     */
    protected abstract Double processCalculation(Iterable collection, String[] extraParam);
    
    
    /**
     * Convert the input into an iterable instance.
     * @param input
     * @return 
     */
    private Iterable convertInput(Object input) {
        return new UniversalIterable(input);
    }
    
    /**
     * Get the actual number from a property. An empty or null extra param means
     * return the same object.
     * 
     * @param value
     * @param extraParam
     * @return 
     */
    protected Double getActualValue(Object value, String extraParam) {

        if (value == null) {
            return 0.0;
        }

        if (StringUtils.isEmpty(extraParam)) {
            return ((Number) value).doubleValue();
        } else {
            return ((Number) modifier.readPropertyValue(extraParam, value)).doubleValue();
        }
    }
    
    //BEAN MODIFIER AWARE CODE
    protected BeanModifier modifier;

    @Override
    public void setBeanModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }
}
