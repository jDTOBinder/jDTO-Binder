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

import groovy.lang.Binding;
import java.util.List;
import org.jdto.MultiPropertyValueMerger;

/**
 * Merge the source values by evaluating a Groovy Expression.<br />
 * 
 * This merger uses the embedded Groovy engine to provide convenience and 
 * simplicity to specify complex transformations. <br />
 * 
 * In order to access the input of this merger, a scoped variable called
 * <code>sourceValues</code> has been introduced.<br />
 * 
 * For convenience, the following packages have been imported: 
 * 
 * <ul>
 *  <li>org.apache.commons.lang.*</li>
 *  <li>java.util.*</li>
 *  <li>java.math.*</li>
 * </ul>
 * 
 * Finally, a scoped variable called <code>logger</code> has been introduced to help
 * with logging.
 * 
 * Here are some examples:<br />
 * 
 * <pre>
 * "sourceValues.toString()" //convert to string.
 * "sourceValues[1] //return the second value.
 * "sourceValues[0].getName() + sourceValues[1].toString()" //concatenate the first and second value.
 * "logger.error('Values are: ' + sourceValues); return sourceValues;" //log the values and return them.
 * </pre>
 * 
 * @author Juan Alberto López Cavallotti
 */
public class MultiGroovyMerger extends AbstractGroovyMerger implements MultiPropertyValueMerger<Object> {
    private static final long serialVersionUID = 1L;
    
    private static final String SOURCES_VARIABLE_NAME = "sourceValues";
    
    /**
     * Merge a list of source values by aplying the groovy expression sent
     * as the first merger parameter. <br />
     * @param values the list of values in the order defined by the mapping.
     * @param extraParam the first param is the the groovy expression to evaluate.
     * @return the result of evaluating the expression.
     */
    @Override
    public Object mergeObjects(List<Object> values, String[] extraParam) {
        
        String expression = getExpression(extraParam);
        
        //create a new groovy binding.
        Binding binding = new Binding();
        
        //set the source variable name.
        binding.setVariable(SOURCES_VARIABLE_NAME, values);
        
        return evaluateGroovyScript(binding, expression);
    }
    
}
