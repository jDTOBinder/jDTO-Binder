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
package org.jdto.mergers;

import org.apache.commons.lang.ArrayUtils;

/**
 * Merge all the parameters on a collection or array by adding them one to
 * each other. All of the numbers on the collections will be treated as a double.
 * If the collection passed in as a parameter is not a number but rather a complex
 * type, then the extra parameter will decide which property will be read. <br />
 * 
 * This method resolves its results as a Double.
 * @author Juan Alberto Lopez Cavallotti
 */
public class SumMerger extends AbstractCalulationCollectionMerger {

    @Override
    protected Double processCalculation(Iterable collection, String[] extraParams) {
        
        double sum = 0.0;
        
        if (ArrayUtils.isEmpty(extraParams)) {
            throw new IllegalArgumentException("Property name is required");
        }
        
        String extraParam = extraParams[0];
        
        for (Object value : collection) {

            //get the double.
            Double doubleValue = getActualValue(value, extraParam);
            sum += doubleValue;
        }

        return sum;
    }
}
