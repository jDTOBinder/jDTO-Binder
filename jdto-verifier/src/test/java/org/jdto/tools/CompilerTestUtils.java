/*
 * Copyright 2013 Juan Alberto López Cavallotti.
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

package org.jdto.tools;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Utility methods that help when invoking the java compiler.
 * @author Juan Alberto López Cavallotti
 */
public class CompilerTestUtils {
    
    /**
     * Delete generated class files after the compilation has completed. 
     * 
     * Basically this mehtod looks for java files and if they exist check if there
     * is a matching .class file, deleting it afterwards.
     * 
     * @param startIndex the index from where to start reading.
     * @param compilerArgs the list of compiler args that had been used to invoke
     * the java compiler.
     */
    public static void cleanClassFiles(int startIndex, String[] compilerArgs) {
        
        if (compilerArgs.length == 0) {
            return;
        }
        
        if (startIndex < 0 || startIndex >= compilerArgs.length) {
            startIndex = 0;
        }
        
        for (int i = startIndex; i < compilerArgs.length; i++) {
            File f = new File(compilerArgs[i]);
            
            //if no java file, no possible class file.
            if (!f.exists()) {
                continue;
            }
            
            String classFile = StringUtils.replace(compilerArgs[i], ".java", ".class");
            
            f = new File(classFile);
            
            if (f.exists() && f.isFile()) {
                f.delete();
            }
            
        }
        
    }
    
}
