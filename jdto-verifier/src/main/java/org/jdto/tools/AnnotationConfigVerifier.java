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

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.jdto.annotation.Source;

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

        TypeElement annotationElement = annotations.iterator().next();

        Set<? extends Element> elms = roundEnv.getElementsAnnotatedWith(annotationElement);

        //at this point we have all the DTO's annotated with @DTOVerify
        for (Element element : elms) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Validating: " + element.toString());

            TypeElement targetType = extractTargetType(element, annotationElement, messager);

            validateDTO((TypeElement) element, targetType, messager);
        }



        return true;
    }

    private void validateDTO(TypeElement element, TypeElement targetType, Messager msg) {
        //configuration ought to be on the getter, the setter or the field.
        //since we're copying from beans first inspect the getters/setters

        //go trough all the getters.
        List<? extends Element> elms = element.getEnclosedElements();

        for (Element enclElement : elms) {

            //we're interested on getters.
            if (elementIsRelevant(enclElement)) {
                
                String sourceProperty = extractSourceProperty(element, enclElement);
                validateElementConfiguration(sourceProperty, enclElement, targetType, msg);
            }

        }

    }

    /*
     * This needs proper validation.
     */
    private boolean elementIsRelevant(Element enclElement) {

        if (enclElement.getKind() != ElementKind.METHOD) {
            return false;
        }

        String strRep = enclElement.getSimpleName().toString();

        if (StringUtils.startsWith(strRep, "get") || StringUtils.startsWith(strRep, "is")) {
            return true;
        }

        return false;
    }

    /*
     * This needs to take more arguments for proper validations.
     */
    private void validateElementConfiguration(String sourceProperty, Element getter, TypeElement targetObject, Messager messager) {

        //means unknown configuration.
        if (sourceProperty == null) {
            return;
        }

        //the target object should have the getter.
        if (!hasGetter(sourceProperty, targetObject)) {
            messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Property not found on source Object: " + sourceProperty, getter);
        }

    }

    private TypeElement extractTargetType(Element element, TypeElement annotationElement, Messager messager) {

        List<? extends AnnotationMirror> am = element.getAnnotationMirrors();

        for (AnnotationMirror annotationMirror : am) {
            if (annotationMirror.getAnnotationType().equals(annotationElement.asType())) {

                //the annotation has only one argument so is easy to extract the value
                Map<? extends ExecutableElement, ? extends AnnotationValue> map = processingEnv.getElementUtils().getElementValuesWithDefaults(annotationMirror);

                //iterate and return the first value.
                for (ExecutableElement executableElement : map.keySet()) {

                    AnnotationValue val = map.get(executableElement);

                    String type = val.getValue().toString();

                    return super.processingEnv.getElementUtils().getTypeElement(type);
                }

                return null;

            }
        }

        messager.printMessage(Diagnostic.Kind.ERROR, "Could not find target class on element", element);
        return null;
    }

    private String extractSourceProperty(TypeElement element, Element enclElement) {

        //check for annotations.
        Source annot = enclElement.getAnnotation(Source.class);

        if (annot != null) {
            return annot.value();
        }

        //normalize the value.
        String name = enclElement.getSimpleName().toString();

        name = (name.startsWith("is")) ? name.substring(2) : name.substring(3);

        //uncapitalize.
        name = StringUtils.uncapitalize(name);

        //at this point the annotaiton is either on the field or not present.
        Element field = findFieldOnType(element, name);

        //if no field is found, that is a valid configuration. return the name of the property.
        if (field == null) {
            return name;
        }

        //if no annotation is present on the field then also return the field name.
        annot = field.getAnnotation(Source.class);

        if (annot == null) {
            return name;
        }

        return annot.value();
    }

    private Element findFieldOnType(TypeElement element, String name) {

        for (Element enclosedElement : element.getEnclosedElements()) {

            if (enclosedElement.getKind() == ElementKind.FIELD && name.equals(enclosedElement.getSimpleName().toString())) {
                return enclosedElement;
            }

        }

        return null;
    }

    private Element findGetterOnType(TypeElement element, String name) {

        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) {
                continue;
            }

            String elementName = enclosedElement.getSimpleName().toString();

            if (elementName.startsWith("get")) {

                elementName = StringUtils.uncapitalize(elementName.substring(3));

                if (name.equals(elementName)) {
                    return enclosedElement;
                }
            }

            if (elementName.startsWith("is")) {

                elementName = StringUtils.uncapitalize(elementName.substring(2));

                if (name.equals(elementName)) {
                    return enclosedElement;
                }
            }

        }

        return null;
    }

    private Element findSetterOnType(TypeElement element, String name) {
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) {
                continue;
            }

            String elementName = enclosedElement.getSimpleName().toString();

            if (elementName.startsWith("set")) {

                elementName = StringUtils.uncapitalize(elementName.substring(3));

                if (name.equals(elementName)) {
                    return enclosedElement;
                }
            }

        }

        return null;
    }

    private boolean hasGetter(String sourceProperty, TypeElement targetObject) {
        return findGetterOnType(targetObject, sourceProperty) != null;
    }
}
