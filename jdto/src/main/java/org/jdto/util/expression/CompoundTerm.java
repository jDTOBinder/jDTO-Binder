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

/**
 * Represents a compound term of an expression. A compound term is of the type
 * of terms that take two terms and an operator to produce some result.
 * @author Juan Alberto Lopez Cavallotti
 */
final class CompoundTerm implements ExpressionTerm, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Operator operator;
    private final ExpressionTerm leftSide;
    private final ExpressionTerm rightSide;
    
    /**
     * Create a new unstance of a compond term
     * @param operator
     * @param leftSide
     * @param rightSide 
     */
    public CompoundTerm(Operator operator, ExpressionTerm leftSide, ExpressionTerm rightSide) {
        this.operator = operator;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }
    
    @Override
    public double evaluate() {
        return operator.apply(leftSide, rightSide);
    }
    
}
