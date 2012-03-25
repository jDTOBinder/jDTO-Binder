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

import org.jdto.BeanModifier;
import org.jdto.BeanModifierAware;
import org.jdto.SinglePropertyValueMerger;
import org.jdto.impl.InstancePool;
import org.jdto.util.expression.Expression;
import org.apache.commons.lang.ArrayUtils;

/**
 * Single property value merger which takes a math expression by param and
 * evaluates it obtaining the variables from the value to be merged. <br />
 * If the value to be merged is a number, then every variable on the expression
 * will be replaced with the value. Even though this behavior may look awkward
 * it is only enabled for the pupose to apply some formula to a single number
 * without making the user to put the number on other instance.
 * 
 * <br />
 * Example:
 * 
 * <ul>
 * <li>if value = 10 and extraParam ("variable * 1.5") then the result is 15.</li>
 * </ul>
 * 
 * In the previous example the value could be named with any name, increasing
 * code readability. <br />
 * 
 * Resulting expressions are chached locally to the merger instance to avoid
 * trouble with other mergers.
 * @author Juan Alberto Lopez Cavallotti
 */
public class ExpressionMerger implements SinglePropertyValueMerger<Double, Object>, BeanModifierAware {

    private static final String MERGER_PREFIX = ExpressionMerger.class.getName();

    @Override
    public Double mergeObjects(final Object value, String[] extraParams) {
        
        if (ArrayUtils.isEmpty(extraParams)) {
            throw new IllegalArgumentException("Expression parameter is required");
        }
        
        String extraParam = extraParams[0];
        
        Expression exp = InstancePool.getNamedInstance(MERGER_PREFIX + extraParam);

        if (exp == null) {
            exp = new Expression(extraParam, new Expression.VariableResolver() {

                @Override
                public Number resolveVariable(String variable) {
                    if (Number.class.isAssignableFrom(value.getClass())) {
                        return (Number) value;
                    } else {
                        return (Number) modifier.readPropertyValue(variable, value);
                    }
                }
            });
            InstancePool.putNamedInstance(MERGER_PREFIX + extraParam, exp);
        }

        return exp.evaluate();
    }
    private BeanModifier modifier;

    @Override
    public void setBeanModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }
}
