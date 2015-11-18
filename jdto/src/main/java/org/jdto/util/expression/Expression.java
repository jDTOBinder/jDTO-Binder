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

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Represents a math expression, this expression can be evaluated in order to
 * obtain a result. <br />
 * The expression evaluation algorithm will go through some stages to convert
 * the formula in something easily evaluable. <br />
 * You may cache this formula to obtain performance gains in your application.
 * @author Juan Alberto Lopez Cavallotti
 */
public final class Expression implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //for variable resolution.
    private final HashMap<String, Number> varValues;
    
    //the root term.
    private final ExpressionTerm rootTerm;
    
    private final VariableResolver resolver;
    
    /**
     * Convenience constructor useful for parameterized expressions. Calling 
     * this constructor is equals to calling Expression(exp, null).
     * @param expression 
     */
    public Expression(String expression) {
        this(expression, null);
    }
    
    /**
     * Parse the math expression passed as an argument and build a new instance
     * of the Expression object.
     * @param expression the expression to be parsed.
     * @param resolver the instance used to resolve variable values, can be null.
     * @throws IllegalArgumentException if the exception could not be parsed.
     */
    public Expression(String expression, VariableResolver resolver) {
        this.varValues = new HashMap<String, Number>();
        this.resolver = resolver;
        this.rootTerm = parseExpression(expression);
    }
    
    //global variable of where we are parsing the string
    //this should be on the parseExpression method but here is more convenient
    //at least on the overall process
    private int position = 0;
    
    /**
     * Parse the expression into something easily evaluable.
     * @param expression
     * @return 
     */
    private synchronized ExpressionTerm parseExpression(String expression) {
        
        position = 0;
        
        LinkedList<String> precedenceStack = new LinkedList<String>(); 
        
        
        //add the first imaginary parentheses.
        precedenceStack.push("(");
        
        //append a closing parenthesis to the expression.
        expression = expression + ")";
        
        //the previous token.
        String token = null;
        
        StringBuilder postFix = new StringBuilder();
        
        /**
         * Go through the expression.
         */
        while(!precedenceStack.isEmpty() && position < expression.length()) {
            
            //use the token from previous iteration
            token = readToken(token, expression);
            
            //if is a left parentheses
            if ("(".equals(token)) {
                precedenceStack.push(token);
                postFix.append(" "); //a separation
                continue;
            }
            
            //check if it is an operator
            Operator operator =  Operator.getOperaorByString(token);
            if (operator != null) {
                postFix.append(" "); //add a seprarator char to the result.
                while(operator.precedence(precedenceStack.peek())) {
                    postFix.append(precedenceStack.pop());
                    postFix.append(" ");
                }
                precedenceStack.push(token);
                continue;
            }
            
            //check if it is a right parenthesis
            if (")".equals(token)) {
                postFix.append(" "); //add a separator to the result.
                while (!"(".equals(precedenceStack.peek())) {
                    String stackElement = precedenceStack.pop();
                    
                    if (isOperator(stackElement)) {
                        postFix.append(stackElement);
                        postFix.append(" ");
                    } 
                }
                //remove the extra parenthesis
                precedenceStack.pop();
                continue;
            }
            
            //if everything else fails, just add the token to the postfix expr
            postFix.append(token);
            //and we're done with the loop here
        }
        
        //at this point we need to convert the postfix expression into terms.
        if (!precedenceStack.isEmpty()) {
           throw new IllegalArgumentException("Could not parse expression!"); 
        }
        
        return parsePostfixExpr(postFix.toString());
    }

    private String readToken(String previousToken, String expression) {
        
        boolean operatorAllowed = true;
        //logic for negative numbers
        if (previousToken == null || previousToken.equals("(") || isOperator(previousToken)) {
            operatorAllowed = false;
        }
        
        StringBuilder tokenBuilder = null;
        
        //convenience variable
        while(position < expression.length()) {
            //get and increment the character
            char character = expression.charAt(position++);
            
            //if is space, then ignore it
            if (character == ' ') {
                continue;
            }
            
            //if is parentheses
            if (character == '('  || character == ')') {
                return Character.toString(character);
            }
            
            if (isOperator(character) && operatorAllowed) {
                return Character.toString(character);
            }
            
            if (isOperator(character) && character != '-') {
                throw new IllegalArgumentException("Operator "+character+" not allowed here");
            }
            
            //if none of the previous, then read a nuber or variable.
            tokenBuilder = getOrCreateBuilder(tokenBuilder);
            tokenBuilder.append(character);
            while(position < expression.length()) {
                character = expression.charAt(position++);
                if (character == ' ') {
                    //this character is not important
                    break;
                }
                if (character == ')' || isOperator(character)) {
                    position--;
                    break;
                } else {
                    tokenBuilder.append(character);
                }
            }
            return tokenBuilder.toString();
        }
        return null;
    }
    
    public double evaluate() {
        return rootTerm.evaluate();
    }
    
    private boolean isOperator(String previousToken) {
        if (StringUtils.length(previousToken) != 1) {
            return false;
        }
        
        char character = previousToken.charAt(0);
        
        //look if it is on the list of supported operators.
        return isOperator(character);
    }
    
    /**
     * Check if a character is an operator.
     * @param character
     * @return 
     */
    private boolean isOperator(char character) {
        return Operator.getOperaorByCharacter(character) != null;
    }

    /**
     * Get or create a string builder.
     * @param builder
     * @return 
     */
    private StringBuilder getOrCreateBuilder(StringBuilder builder) {
        if (builder == null) {
            return new StringBuilder();
        } else {
            return builder;
        }
    }

    private ExpressionTerm parsePostfixExpr(String postfix) {
        //split the string
        String[] tokens = StringUtils.split(postfix, ' ');
        
        LinkedList<ExpressionTerm> termStack = new LinkedList<ExpressionTerm>();
        
        for (String token : tokens) {
            //if is not an operator, then read a literal or variable
            if (!isOperator(token)) {
                termStack.push(buildTerm(token));
            } else {
                //try to build a compound term and push it.
                Operator op = Operator.getOperaorByString(token);
                ExpressionTerm right = termStack.pop(); //first is the right side
                ExpressionTerm left = termStack.pop(); //and then the left side
                
                ExpressionTerm term = new CompoundTerm(op, left, right);
                termStack.push(term);
            }
        }
        
        //at this point the stack should have just one element.
        
        return termStack.pop();
    }

    private ExpressionTerm buildTerm(String token) {
        if (token.matches("[A-Za-z]{1}+[.[A-Za-z]]*")) {
            return new VariableTerm(token, varValues, resolver);
        } else {
            //try to parse or let the user blow with a number format exception
            return new LiteralTerm(Double.parseDouble(token));
        }
    }

    /**
     * Set the value of a variable by name.
     * @param var the name of the variable.
     * @param value the value of the variable.
     */
    public void setVariable(String var, Number value) {
        varValues.put(var, value);
    }
    
    /**
     * Get all of the variable names for this expresion.
     * @return the list of variable names found on the expression.
     */
    public Set<String> getVariableNames() {
        return varValues.keySet();
    }
    
    /**
     * Implementations should know how to resolve a value from a variable name.
     */
    public static interface VariableResolver {
        /**
         * Resolve a variable out of a variable name.
         * @param variable
         * @return the value resolved using the variable name.
         */
        Number resolveVariable(String variable);
    }
    
}
