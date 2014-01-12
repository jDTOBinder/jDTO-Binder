/*
 * Copyright 2014 Juan Alberto López Cavallotti.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jdto.mergers;

import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdto.MultiPropertyValueMerger;
import org.jdto.SinglePropertyValueMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge the source value by evaluating a Script Expression. <br />
 * 
 * This merger uses the JSR 223 API to evaluate a script using a scripting 
 * engine. <br />
 * 
 * Thanks to this API, this merger may use various script engines, configured
 * by different means, to evaluate expressions in the user's preferred script 
 * engine. <br />
 * 
 * The value to be merged is available through the <b><pre>sourceValue</pre></b>
 * local variable. <br />
 * 
 * To make a difference between this merger and the 
 * {@link GroovyMerger GroovyMerger}, this does not assume any
 * utility imports and relies on the ones provided by the script engine (if such
 * thing makes sense), this decision is taken to maximize compatibility. <br />
 * 
 * Finally, any exceptions thrown by the script manager will be wrapped in a
 * {@link RuntimeException RuntimeException} to maximize API compatibility.
 * 
 * @author Juan Alberto López Cavallotti
 * @since 1.5
 */
public class ScriptMerger implements SinglePropertyValueMerger<Object, Object>, MultiPropertyValueMerger<Object> {
    
    /**
     * The default script engine this merger will use.
     */
    public static final String DEFAULT_SCRIPT_ENGINE = "js";
    
    private static final Logger logger = LoggerFactory.getLogger(ScriptMerger.class);
    
    /**
     * Merge the input object by evaluating a scripting expression given on the
     * first extraParam. The result is the execution of the expression by the
     * selected script engine. <br />
     * 
     * If extraParam array has a second element, then it must be a valid script
     * engine name, this script engine will be used to evaluate the script on
     * the first parameter. <br />
     * 
     * @param value the value to merge. This value will be available to the 
     * script expression under the 'sourceValue' variable.
     * @param extraParam the first element of this array is the script to execute
     * the optional second element is the name of the scripting engine to use.
     * 
     * @return The result of executing the expression.
     * 
     * @throws RuntimeException when the script execution fails.
     */
    @Override
    public Object mergeObjects(Object value, String[] extraParam) {
        
        //delegate to 'the one' method.
        return doMergeObjects(value, extraParam);
    }

    /**
     * Merge the input objects by evaluating a scripting expression given on the
     * first extraParam. The result is the execution of the expression by the
     * selected script engine. <br />
     * 
     * If extraParam array has a second element, then it must be a valid script
     * engine name, this script engine will be used to evaluate the script on
     * the first parameter. <br />
     * 
     * @param values the values to merge. This value will be available to the 
     * script expression under the 'sourceValues' variable.
     * @param extraParam the first element of this array is the script to execute
     * the optional second element is the name of the scripting engine to use.
     * 
     * @return The result of executing the expression.
     * 
     * @throws RuntimeException when the script execution fails.
     */    
    @Override
    public Object mergeObjects(List<Object> values, String[] extraParam) {
        //delegate to 'the one' method
        return doMergeObjects(values, extraParam);
    }    
    
    @Override
    public boolean isRestoreSupported(String[] params) {
        return false;
    }

    @Override
    public Object restoreObject(Object object, String[] params) {
        return null;
    }
    
    private ScriptEngine initializeScriptEngine(String[] extraParam) {
        String engineName = DEFAULT_SCRIPT_ENGINE;
        
        //the script engine may be configured as the second parameter.
        if (extraParam.length > 1 && !StringUtils.isEmpty(extraParam[1])) {
            engineName = extraParam[1];
        }
        
        //TODO - REVIEW IF THIS IS SAFE TO INSTANTIATE ALL THE TIME
        ScriptEngineManager scriptEngineMgr = new ScriptEngineManager();
        
        //TODO - REVIEW IF THIS IS SAFE TO INSTANTIATE ALL THE TIME
        ScriptEngine engine = scriptEngineMgr.getEngineByName(engineName);
        
        if (engine == null) {
            logger.error("Engine " + engineName + " not found!.");
            throw new IllegalArgumentException("No scripting engine "
                    + "could be found with name: " + engineName);
        }
        
        return engine;
    }
    
    private String extractScript(String[] extraParam) {
        
        if (ArrayUtils.isEmpty(extraParam) || StringUtils.isEmpty(extraParam[0])) {
            logger.error("Invalid script to evaluate");
            throw new IllegalArgumentException("You need to configure the "
                    + "script expression to be evaluated.");
        }
        
        return extraParam[0];
    }
    
    private Object executeScript(ScriptEngine engine, String script) {
        
        try {
            return engine.eval(script);
        } catch (ScriptException ex) {
            String engineName = engine.toString();
            logger.error("Could not execute script in engine: " + engineName, ex);
            throw new RuntimeException("Failure while executing script", ex);
        }
    }
    
    
    private Object doMergeObjects(Object value, String[] extraParam) {
        String script = extractScript(extraParam);
        ScriptEngine engine = initializeScriptEngine(extraParam);
        
        //populate local variables
        engine.put("sourceValue", value);
        
        //if the value is a list then, just make it available as sourceValues
        if (value instanceof List) {
            engine.put("sourceValues", value);
        }
        
        //try to execute the script.
        return executeScript(engine, script);
    }
}
