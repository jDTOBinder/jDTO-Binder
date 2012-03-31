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
package org.jdto.mergers;

import org.jdto.impl.InstancePool;
import org.jdto.util.expression.Expression;
import org.apache.commons.lang.ArrayUtils;

/**
 * Expression merger which will sum the result of a calculation given by an 
 * expression provided as the extraParam. This method works exactly like
 * {@link ExpressionMerger} but applying the formula for each element of the
 * collection.
 * 
 * You must provide the expression as the extra parameter or an 
 * IllegalArgumentException will be thrown.
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class SumExpressionMerger extends AbstractCalulationCollectionMerger {

    private static final long serialVersionUID = 1L;

    private static final String MERGER_PREFIX = SumExpressionMerger.class.getName();
    
    /**
     * Iterate and sum the results of a expression.
     * @param collection the collection to be iterated.
     * @param extraParams the expression to be evaluated
     * @return The result of applying the sum of the expression results.
     */
    @Override
    protected Double processCalculation(Iterable collection, String[] extraParams) {
        double sum = 0;
        
        if (ArrayUtils.isEmpty(extraParams)) {
            throw new IllegalArgumentException("Expression is required");
        }
        
        String extraParam = extraParams[0];
        
        Expression exp = InstancePool.getNamedInstance(MERGER_PREFIX + extraParam);
        if (exp == null) {
            exp = new Expression(extraParam);
            InstancePool.putNamedInstance(MERGER_PREFIX + extraParam, exp);
        }

        for (Object value : collection) {

            final Object currentValue = value;

            //go though the variable names.
            for (String variable : exp.getVariableNames()) {
                Number varValue = null;
                if (Number.class.isAssignableFrom(currentValue.getClass())) {
                    varValue = (Number) currentValue;
                } else {
                    varValue = (Number) modifier.readPropertyValue(variable, currentValue);
                }
                exp.setVariable(variable, varValue);
            }
            
            //evaluate the formula
            double doubleValue = exp.evaluate();
            //get the double.
            sum += doubleValue;
        }

        return sum;
    }
}
