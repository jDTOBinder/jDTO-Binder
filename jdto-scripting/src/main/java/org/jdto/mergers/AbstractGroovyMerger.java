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
import java.io.Serializable;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.jdto.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for groovy mergers, contains the logic needed to bootstrap the groovy
 * evaluator and the default scoped variables and imports.
 * @author Juan Alberto López Cavallotti
 */
public abstract class AbstractGroovyMerger implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * The name of the variable used for the logger instance.
     */
    public static final String LOGGER_VARIABLE_NAME = "logger";
    
    protected static final CompilerConfiguration currentConfiguration;
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * Evaluate the groovy script.
     */
    protected Object evaluateGroovyScript(Binding binding, String expression) {
        //add the logger.
        binding.setVariable(GroovyMerger.LOGGER_VARIABLE_NAME, logger);
        //create a groovy shell.
        GroovyShell shell = new GroovyShell(binding, GroovyMerger.currentConfiguration);
        //evaluate the groovy expression.
        return shell.evaluate(expression);
    }

    /*
     * Get the expression from the merger parameters.
     */
    protected String getExpression(String[] extraParam) {
        //check for a valid groovy expression.
        if (ArrayUtils.isEmpty(extraParam) || StringUtils.isEmpty(extraParam[0])) {
            logger.error("You should provide a groovy script expression to evaluate.");
            throw new IllegalArgumentException("Invalid Groovy expression");
        }
        //get the groovy expression
        String expression = extraParam[0];
        return expression;
    }

    /**
     * Default implementation for mergers.
     * @param params parameters to take in consideration for restore purposes.
     * @return false, groovy mergers does not support restore.
     */
    public boolean isRestoreSupported(String[] params) {
        return false;
    }
    
    /**
     * Groovy mergers does not support restore. <br />
     * @param object the object to restore.
     * @param params the parrameters used to merge.
     * @return <code>null</code>.
     */
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
