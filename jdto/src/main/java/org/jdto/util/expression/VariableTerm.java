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

package org.jdto.util.expression;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a term that is resolved via a table.
 * @author juancavallotti
 */
class VariableTerm implements ExpressionTerm, Serializable {
    private static final long serialVersionUID = 1L;
    private final String variableName;
    private final HashMap<String, Number> variableMap;
    
    private final Expression.VariableResolver resolver;
    
    /**
     * Build a new instance of the a variable term.
     * @param variableName
     * @param variableMap 
     */
    public VariableTerm(String variableName, HashMap<String, Number> variableMap, Expression.VariableResolver resolver) {
        this.variableName = variableName;
        this.variableMap = variableMap;
        this.resolver = resolver;
        //put the variable name as a key on the variable map
        variableMap.put(variableName, null);
    }
    
    /**
     * Null or not found variables will produce a runtime exception.
     */
    @Override
    public double evaluate() {
        //try to get the value
        Number value = variableMap.get(variableName);
        
        if (value == null && resolver == null) {
            throw new IllegalArgumentException("No value for variable: "+variableName);
        }
        
        if (value == null) {
            value = resolver.resolveVariable(variableName);
        }
        
        if (value == null) {
            throw new IllegalArgumentException("Could not resolve variable: "+variableName);
        }
        
        return value.doubleValue();
    }
    
    
}
