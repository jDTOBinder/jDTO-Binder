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

package org.jdto.tools.verifiercases;

import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import org.apache.commons.lang.StringUtils;
import org.jdto.tools.CompilerTestCase;
import static org.junit.Assert.*;

/**
 *
 * @author juancavallotti
 */
public class SetterAnnotatedVerifierCase implements CompilerTestCase {

    @Override
    public String[] getClassesToCompile() {
        return new String[] {
            "src/test/java/org/jdto/tools/testobjects/SetterDTO.java" 
        };
    }

    @Override
    public void test(List<Diagnostic<? extends JavaFileObject>> diagnostics, String stdoutS, Boolean result) {
        
        //in this case we should have a compilation error saying that annotation has no effect on setters.
        
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
            if (diagnostic.getKind() == Kind.MANDATORY_WARNING) {
                
                if (!StringUtils.containsIgnoreCase(diagnostic.getMessage(null), "Setter")) {
                    continue;
                } else {
                    //test should succeed.
                    return;
                }
                
            }
        }
        
        fail("A warning saying that setters mustn't be annotated should have been printed.");
    }

}
