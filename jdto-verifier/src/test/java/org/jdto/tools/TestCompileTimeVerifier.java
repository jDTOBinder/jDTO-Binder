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

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.jdto.tools.verifiercases.NestedVerifierCase;
import org.jdto.tools.verifiercases.SetterAnnotatedVerifierCase;
import org.jdto.tools.verifiercases.SimpleVerifierCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test case for the compile time verifier.
 *
 * @author Juan Alberto López Cavallotti
 */
@RunWith(Parameterized.class)
public class TestCompileTimeVerifier {

    @Parameters
    public static Collection<Object[]> data() {
        LinkedList<Object[]> ret = new LinkedList<Object[]>();

        ret.add(new Object[]{new SimpleVerifierCase()});
        ret.add(new Object[]{new NestedVerifierCase()});
        ret.add(new Object[]{new SetterAnnotatedVerifierCase()});

        return ret;
    }
    private static JavaCompiler compiler;
    private StandardJavaFileManager fileManager;
    private DiagnosticCollector<JavaFileObject> collector;
    private CompilerTestCase currentTestCase;

    public TestCompileTimeVerifier(CompilerTestCase currentTestCase) {
        this.currentTestCase = currentTestCase;
    }

    @BeforeClass
    public static void initClass() throws Exception {

        //get the java compiler.
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    @Before
    public void initTest() throws Exception {

        //configure the diagnostics collector.
        collector = new DiagnosticCollector<JavaFileObject>();
        fileManager = compiler.getStandardFileManager(collector, Locale.US, Charset.forName("UTF-8"));
    }

    @Test
    public void testCompilation() throws Exception {
        //the files to be compiled.
        String[] files = currentTestCase.getClassesToCompile();
        try {
            //streams.
            ByteArrayOutputStream stdoutStream = new ByteArrayOutputStream();
            OutputStreamWriter stdout = new OutputStreamWriter(stdoutStream);


            JavaCompiler.CompilationTask task = compiler.getTask(stdout, fileManager, collector, null, null, fileManager.getJavaFileObjects(files));

            Boolean result = task.call();

            String stdoutS = new String(stdoutStream.toByteArray());


            //perform the verifications.
            currentTestCase.test(collector.getDiagnostics(), stdoutS, result);
        } finally {
            CompilerTestUtils.cleanClassFiles(0, files);
        }
    }
}
