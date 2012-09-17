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
import groovy.lang.GroovyShell;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.jdto.SinglePropertyValueMerger;
import org.jdto.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge the value by evaluating a Groovy Expression.<br />
 * 
 * This merger uses the Embedded Groovy engine to provide convenience and 
 * simplicity to specify complex transformations. <br />
 * 
 * In order to access the source value of this merger, a scoped variable called
 * <code>sourceValue</code> has been introduced.<br />
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
 * "sourceValue.toString()" //convert to string.
 * "sourceValue == null ? 'isNull' : 'isNotNull'" //check for nulls
 * "StringUtils.reverse(sourceValue)" //Access the StringUtils class from commons.
 * "logger.error('Value is: ' + sourceValue); return sourceValue;" //log the value and return it.
 * </pre>
 * 
 * @author Juan Alberto López Cavallotti
 */
public class GroovyMerger implements SinglePropertyValueMerger<Object, Object> {
    private static final long serialVersionUID = 1L;
    
    private static final String SOURCE_VARIABLE_NAME = "sourceValue";
    
    private static final String LOGGER_VARIABLE_NAME = "logger";
    
    private static final CompilerConfiguration currentConfiguration;
    
    private static final Logger logger = LoggerFactory.getLogger(GroovyMerger.class);
    
    /**
     * Merge the source value by applying the groovy expression sent as the first
     * merger parameter.
     * @param value the object to be merged.
     * @param extraParam the first param is the expression to evaluate.
     * @return the result of the expression.
     */
    @Override
    public Object mergeObjects(Object value, String[] extraParam) {
        
        //check for a valid groovy expression.
        if (ArrayUtils.isEmpty(extraParam) || StringUtils.isEmpty(extraParam[0])) {
            logger.error("You should provide a groovy script expression to evaluate.");
            throw new IllegalArgumentException("Invalid Groovy expression");
        }
        
        //get the groovy expression
        String expression = extraParam[0];
        
        //create a new groovy binding.
        Binding binding = new Binding();
        
        //set the source variable name.
        binding.setVariable(SOURCE_VARIABLE_NAME, value);
        
        //add the logger.
        binding.setVariable(LOGGER_VARIABLE_NAME, logger);
        
        //create a groovy shell.
        GroovyShell shell = new GroovyShell(binding, currentConfiguration);
        
        //evaluate the groovy expression.
        return shell.evaluate(expression);
        
    }

    @Override
    public boolean isRestoreSupported(String[] params) {
        return false;
    }

    @Override
    public Object restoreObject(Object object, String[] params) {
        return null;
    }

    /**
     * Customize the imports.
     * @return the import statements for the groovy shell
     */
    private static ImportCustomizer performImports() {
        ImportCustomizer ic = new ImportCustomizer();
        
        ic.addStarImports(
                "org.apache.commons.lang", //import commons
                "java.util", //import java util
                "java.math" //import java math
                );
        
        return ic;
    }

    static {
        currentConfiguration = new CompilerConfiguration();
        currentConfiguration.addCompilationCustomizers(performImports());
    }
    
}
