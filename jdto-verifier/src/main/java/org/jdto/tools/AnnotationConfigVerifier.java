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

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Annotation processor that verifies the sanity of the DTO Mapping.
 * 
 * This annotation processor will work with source code starting from version 6.
 * 
 * @author Juan Alberto López Cavallotti
 */
@SupportedAnnotationTypes("org.jdto.annotation.DTOVerify")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationConfigVerifier extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Starting jDTO Binder Configuration verifier...");
    }
    
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        Messager messager = processingEnv.getMessager();
        
        if (annotations.isEmpty()) {
            return false;
        }
        
        TypeElement elm = annotations.iterator().next();
        
        Set<? extends Element> elms = roundEnv.getElementsAnnotatedWith(elm);
        
        for (Element element : elms) {
            messager.printMessage(Diagnostic.Kind.NOTE, element.toString());
            
            
            
            
        }
        
        
        
        return true;
    }

}
