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
package org.jdto.util.expression;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the utility class for evaluating math expressions.
 * @author Juan Alberto Lopez Cavallotti
 */
public class TestExpressionEvaluation {

    @Test
    public void testSimpleExpression() {
        String expression = "2 + 5 * 4";
        double expectedResult = 22.0;

        //no variable resolver
        Expression exp = new Expression(expression);
        double result = exp.evaluate();

        assertEquals(expectedResult, result, 0.00001);
    }

    @Test
    public void testNestedExpression() {
        String expression = "(2+5)*-1/1";
        double expectedResult = -7;

        Expression exp = new Expression(expression);
        double result = exp.evaluate();

        assertEquals(expectedResult, result, 0.00001);
    }
    
    @Test
    public void testDivision() {
        String expression = "1/100";
        double expectedResult = 0.01;
        
        Expression exp = new Expression(expression);
        double result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
    }
    
    @Test
    public void testSubstraction() {
        String expression = "100 - 50";
        double expectedResult = 50;
        
        Expression exp = new Expression(expression);
        double result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
    }

    @Test
    public void testPow() {
        String expression = "4 ^ 3";
        double expectedResult = 64;
        
        Expression exp = new Expression(expression);
        double result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
    }
    
    
    
    @Test
    public void testSimpleVariables() {

        final Number a = 4;
        final Number b = 5;

        String expression = "2 + varA * b";
        double expectedResult = 22.0;

        Expression exp = new Expression(expression, new Expression.VariableResolver() {

            @Override
            public Number resolveVariable(String variable) {
                if ("varA".equals(variable)) {
                    return a;
                }
                if ("b".equals(variable)) {
                    return b;
                }
                return 0.0;
            }
        });

        double result = exp.evaluate();

        assertEquals(expectedResult, result, 0.00001);
    }
    
    @Test
    public void testParameterizedExpression() {
        
        String expression = "2*(a + b)+-1.20";
        
        Expression exp = new Expression(expression);
        
        exp.setVariable("a", 5);
        exp.setVariable("b", 4);
        
        double expectedResult = 16.8;
        double result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
        
        //change the variables
        exp.setVariable("a", 10);
        exp.setVariable("b", -5);
        
        expectedResult = 8.8;
        result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
    }
    
    @Test
    public void testVariableRepeatedParameterizedExpression() {
        
        String expression = "amount * price + (amount * price * 0.2)";
        
        Expression exp = new Expression(expression);
        
        exp.setVariable("amount", 5);
        exp.setVariable("price", 10);
        
        double expectedResult = 60.0;
        double result = exp.evaluate();
        
        assertEquals(expectedResult, result, 0.00001);
        
    }
}
