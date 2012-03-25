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
 * Represents a literal term of an expression. This can be built upon a number
 * and it is immutable.
 * @author Juan Alberto Lopez Cavallotti
 */
final class LiteralTerm implements ExpressionTerm, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Number literal;

    public LiteralTerm(Number literal) {
        this.literal = literal;
    }
    
    
    @Override
    public double evaluate() {
        if (literal == null) {
            return 0.0;
        } else {
            return literal.doubleValue();
        }
    }
}
