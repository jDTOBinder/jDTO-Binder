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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;



/**
 * Utility methods for interacting with javax.lang.model classes.
 * @author Juan Alberto López Cavallotti
 */
public class ModelUtils {

    public static Element findFieldOnType(TypeElement element, String name) {

        for (Element enclosedElement : element.getEnclosedElements()) {

            if (enclosedElement.getKind() == ElementKind.FIELD && name.equals(enclosedElement.getSimpleName().toString())) {
                return enclosedElement;
            }

        }

        return null;
    }

    public static ExecutableElement findGetterOnType(TypeElement element, String name) {

        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) {
                continue;
            }

            String elementName = enclosedElement.getSimpleName().toString();
            
            
            if (elementName.startsWith("get")) {

                elementName = StringUtils.uncapitalize(elementName.substring(3));

                if (name.equals(elementName)) {
                    return (ExecutableElement) enclosedElement;
                }
            }

            if (elementName.startsWith("is")) {

                elementName = StringUtils.uncapitalize(elementName.substring(2));

                if (name.equals(elementName)) {
                    return (ExecutableElement) enclosedElement;
                }
            }

        }

        return null;
    }

    public static ExecutableElement findSetterOnType(TypeElement element, String name) {
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.METHOD) {
                continue;
            }

            String elementName = enclosedElement.getSimpleName().toString();

            if (elementName.startsWith("set")) {

                elementName = StringUtils.uncapitalize(elementName.substring(3));

                if (name.equals(elementName)) {
                    return (ExecutableElement) enclosedElement;
                }
            }

        }

        return null;
    }

    public static boolean hasGetter(String sourceProperty, TypeElement targetObject) {
        return findGetterOnType(targetObject, sourceProperty) != null;
    }

}
