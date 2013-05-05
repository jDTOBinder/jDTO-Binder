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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case for the compile time verifier.
 * @author Juan Alberto López Cavallotti
 */
public class TestCompileTimeVerifier {
    
    private static JavaCompiler compiler;
    
    @BeforeClass
    public static void initClass() throws Exception {
        
        //get the java compiler.
        compiler = ToolProvider.getSystemJavaCompiler();
        
        //configure it.
        
        
    }
    
    @Test
    public void testNormalCompile() throws Exception {
        
        //streams.
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        
        //the files to be compiled.
        String filesToCompile[] = {"src/test/java/org/jdto/tools/SimpleEntity.java" , "src/test/java/org/jdto/tools/SimpleDTO.java" };
        
        int result = compiler.run(null, stdout, stderr, filesToCompile);
        
        String stdoutS = new String(stdout.toByteArray());
        String stderrS = new String(stderr.toByteArray());
        
        //finished compiling task lets print the results.
        System.out.println(stdoutS);
        
        System.out.println(stderrS);
        
        
        assertEquals("Files should have no compilation errors", 0, result);
    }
    
}
