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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.jdto.SinglePropertyValueMerger;
import org.jdto.impl.BeanClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Merge the value of any object by calling a no-arg method and returning the
 * result. <br />
 * 
 * The method name should be the first parameter of the parameter array. <br />
 * 
 * The method should be non-void. If the provided value is null, then null will
 * be returned. <br />
 * 
 * The methods on this class will throw unchecked exceptions if misconfiguration 
 * is detected. <br />
 * 
 * This merger is capable of restoring the values, in order to restore, the user
 * must supply a second and optionally a third merger argument. The second argument
 * indicates the name of a class, and the third the name of a static method. In the
 * first scenario, the merger will try to call a constructor on the class to build
 * the restored object; on the second scenario, a static method will be invoked
 * with the object to be restored as a parameter. <br />
 * 
 * @author Juan Alberto Lopez Cavallotti
 * @since 1.1
 */
public class MethodCallMerger implements SinglePropertyValueMerger<Object, Object> {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(MethodCallMerger.class);
    
    /**
     * Merge the value of any object by calling a no-arg and non-void method. <br />
     * Return the value resuting from the method invocation.
     * 
     * @param value the target object whos method will be invoked.
     * @param extraParam the frist value should be the method of the name to be called.
     * @return the result of invoking this non-void method.
     * @throws Subclasses of {@link java.lang.RuntimeException RuntimeException} 
     * for different error conditions.
     */
    @Override
    public Object mergeObjects(Object value, String[] extraParam) {
        if (value == null) {
            return null;
        }
        
        //check if the method name is present.
        if (ArrayUtils.isEmpty(extraParam)) {
            throw new IllegalArgumentException("You must provide a method to call as the first parameter.");
        }
        try {
            Class cls = value.getClass();
            Class[] parms = new Class[0];
            
            //check in the inheritance tree.
            while(!cls.equals(Object.class)) {
                Method method = MethodUtils.getAccessibleMethod(cls, extraParam[0], parms);
                if (method == null) {
                    cls = cls.getSuperclass();
                    continue;
                }
                return method.invoke(value);
            }
            
            throw new IllegalArgumentException("Method with name: "+extraParam[0]+" not found.");
        } catch (Exception ex) {
            logger.error("Error while trying to invoke method: "+extraParam[0], ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isRestoreSupported(String[] params) {
        return params.length == 2 || params.length == 3;
    }

    @Override
    public Object restoreObject(Object object, String[] params) {
        
        if (object == null) {
            return null;
        }
        
        //the second param on the array should be the class to return.
        Class cls = BeanClassUtils.safeGetClass(params[1]);
        
        //if there is a third parameter, then we need to find a factory method.
        //otherwise we need a constructor.
        Class[] types = {object.getClass()};
        if (params.length == 3) {
            //get a static method.
            Object[] args = {object};
            return BeanClassUtils.invokeStaticMethod(cls, params[2], args);
        } else {
            //get a constructor.
            ArrayList arguments = new ArrayList();
            arguments.add(object);
            Constructor c = BeanClassUtils.safeGetConstructor(cls, types);
            return BeanClassUtils.createInstance(cls, c, arguments);
        }
        
    }

}
