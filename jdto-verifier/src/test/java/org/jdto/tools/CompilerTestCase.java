/*
 * Copyright 2013 juancavallotti.
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

import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * SPI for testing compilation errors.
 * @author juancavallotti
 */
public interface CompilerTestCase {
    
    /**
     * Retrieve the list of files whose compilation would be tested.
     * @return a list of files in relative or absolute position.
     */
    public String[] getClassesToCompile();
    
    /**
     * Perform the test.
     * 
     * @param diagnostics the compiler diagnostics for the evaluated files.
     * @param stdoutS  the output of the compiler.
     * @param result the result of the compilation. True if succeeded, false if not.
     */
    public void test(List<Diagnostic<? extends JavaFileObject>> diagnostics, String stdoutS, Boolean result);
    
}
