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

import java.util.HashMap;

/**
 * The supported operators by the evaluation system.
 * @author juancavallotti
 */
enum Operator {
    
    ADDITION('+', 1) {

        @Override
        public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
            return leftSide.evaluate() + rightSide.evaluate();
        }
    
    },
    SUBSTRACTION('-', 1){
        

        @Override
        public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
            return leftSide.evaluate() - rightSide.evaluate();
        }
    
    },
    MULTIPLICATION('*', 2){

        @Override
        public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
            return leftSide.evaluate() * rightSide.evaluate();
        }
    
        
    },
    DIVISION('/', 2){
        

        @Override
        public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
            return leftSide.evaluate() / rightSide.evaluate();
        }
    
    },
    POW('^', 3){
        

        @Override
        public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
            return Math.pow(leftSide.evaluate(), rightSide.evaluate());
        }

    },
    
    ;
    
    //map by character
    private static HashMap<Character, Operator> operators;
    //map by string
    private static HashMap<String, Operator> operatorsStr;
    
    private final char symbol;
    private final int priority;
    
    private Operator(char symbol, int priority) {
        mapOperator(symbol);
        this.symbol = symbol;
        this.priority = priority;
    }

    private synchronized void mapOperator(Character symbol) {
        
        //in-time init of the map.
        if (operators == null) {
            operators = new HashMap<Character, Operator>();
            operatorsStr = new HashMap<String, Operator>();
        }
        
        operators.put(symbol, this);
        operatorsStr.put(symbol.toString(), this);
    }
    
    /**
     * Apply this operator to a given set of expression terms.
     * @param leftSide
     * @param rightSide
     * @return 
     */
    public double apply(ExpressionTerm leftSide, ExpressionTerm rightSide) {
        throw new UnsupportedOperationException("this is not supported");
    }

    public Character getSymbol() {
        return symbol;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
    
    /**
     * Get an operator by its character key
     * @param character
     * @return 
     */
    public static Operator getOperaorByCharacter(char character) {
        return operators.get(character);
    }
    /**
     * Get an operator by its character key
     * @param character
     * @return 
     */
    public static Operator getOperaorByString(String character) {
        return operatorsStr.get(character);
    }
    
    /**
     * Check if an operator is supported or not.
     * @param character
     * @return 
     */
    public static boolean isSupportedOperator(char character) {
        return operators.containsKey(character);
    }
    
    /**
     * Check the precedence of an operator with the string representation of 
     * another. Returns true if the current operator has less priority
     * than the operator repsesented by the argument.
     * @param operator
     * @return true if the priority of this operator is lower than the priority
     * of the other operator, or false if the argument is not an operator.
     */
    boolean precedence(String operator) {
        Operator other = getOperaorByString(operator);
        
        if (other == null) {
            return false;
        }
        
        return priority < other.priority;
    }
    
}
