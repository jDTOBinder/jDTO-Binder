/*
 *    Copyright 2011 Juan Alberto López Cavallotti
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

package org.jdto.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdto.annotation.*;

/**
 * Class for annotation configuration. This will serve as the default bean 
 * inspector. <br />
 * No annotations means default behavior.
 * @author Juan Alberto Lopez Cavallotti
 */
class AnnotationBeanInspector extends AbstractBeanInspector {
    private static final long serialVersionUID = 1L;

    @Override
    FieldMetadata buildFieldMetadata(String propertyName, Method readAccessor, Class beanClass) {

        //creste the default field metadata and override where necessary.
        FieldMetadata ret = buildDefaultFieldMetadata(propertyName);

        //we have the settings so we can apply them.
        HashMap<Class, Annotation> propertyAnnotations = readAnnotations(propertyName, readAccessor, beanClass);

        applySettingsToFieldMetadata(ret, propertyName, null, readAccessor, propertyAnnotations);
        
        applyMergerSettings(propertyName, propertyAnnotations, ret);
        
        return ret;
    }

    @Override
    String[] readSourceBeanNames(Class beanClass) {        
        return readSourceBeanNamesFromAnnotation((SourceNames)beanClass.getAnnotation(SourceNames.class));
    }

    /**
     * Read annotations from the accessor method and from the field, if exists!
     * @param propertyName
     * @param readAccessor
     * @param beanClass
     * @return 
     */
    private HashMap<Class, Annotation> readAnnotations(String propertyName, Method readAccessor, Class beanClass) {

        HashMap<Class, Annotation> ret = new HashMap<Class, Annotation>();

        //try with the method annotations.
        readMemberAnnotations(ret, readAccessor);

        //if the field exists, then read its annotations
        Field matchingField = BeanPropertyUtils.readSafeField(beanClass, propertyName);

        //if the field is found, then it can be annotated.
        if (matchingField != null) {
            readMemberAnnotations(ret, matchingField);
        }
        return ret;
    }

    /**
     * Try to read config annotations from a method or field to a hash map 
     * provided as parameter.
     * @param ret
     * @param annotatedElement 
     */
    private void readMemberAnnotations(HashMap<Class, Annotation> ret, AnnotatedElement annotatedElement) {

        //read the transient annotation.
        populateAnnotation(ret, DTOTransient.class, annotatedElement);

        //read the simple mapping annotation
        populateAnnotation(ret, Source.class, annotatedElement);

        //read the compound mapping annotation
        populateAnnotation(ret, Sources.class, annotatedElement);

        //read the cascade annotation.
        populateAnnotation(ret, DTOCascade.class, annotatedElement);

        //the source names for a compound field.
        populateAnnotation(ret, SourceNames.class, annotatedElement);
    }

    /**
     * Try to read an annotation of a given type from an annotated element and populate a map.
     * @param <T>
     * @param annotations
     * @param annotationType
     * @param element 
     */
    private <T extends Annotation> void populateAnnotation(HashMap<Class, Annotation> annotations, Class<T> annotationType, AnnotatedElement element) {
        T annotationInstance = element.getAnnotation(annotationType);
        if (annotationInstance != null) {
            annotations.put(annotationType, annotationInstance);
        }
    }

    private void applySettingsToFieldMetadata(FieldMetadata target, String propertyName, Class constructorArgType, Method readAccessor, HashMap<Class, Annotation> annotations) {

        //by default, this is bound to the property name.
        //we may not want this on future steps but for now it's ok
        List<String> sources = target.getSourceFields();

        //check if the field is DTOTransient
        if (annotations.containsKey(DTOTransient.class)) {
            target.setFieldTransient(true);
            return;
        }

        //read the settings annotations.
        Source simpleMapping = (Source) annotations.get(Source.class);
        Sources compoundMapping = (Sources) annotations.get(Sources.class);
        DTOCascade cascade = (DTOCascade) annotations.get(DTOCascade.class);

        //perform some validations.
        if (simpleMapping != null && compoundMapping != null) {
            throw new IllegalStateException("Source and Sources annotation present at the same time");
        }

        //if the simple mapping is present then map it.
        if (simpleMapping != null) {
            sources.clear(); //remove the default vaue
            String sourceField = applyMapping(simpleMapping, propertyName);
            sources.add(sourceField);
        }

        //if the compound mapping is present, then map it.
        if (compoundMapping != null) {
            sources.clear(); //remove the default value.
            
            target.initSourceFieldWithSize(compoundMapping.value().length);
            
            int sourceBeanIndex = 0;
            for (Source source : compoundMapping.value()) {
                String sourceField = applyMapping(source, propertyName);
                sources.add(sourceField);
                //set the merger for this field.
                target.setSinglePropertyValueMerger(sourceField,
                        (Class)source.merger(),
                        source.mergerParam(),
                        source.sourceBean(),
                        sourceBeanIndex);
                
                //increase the number of source bean.
                sourceBeanIndex++;
            }
        }

        //if the cascade is present, then apply the conditions for cascading.
        //the constructor arg type and the read accessor can be both null.
        if (cascade != null) {
            applyCascadeLogic(cascade.targetType(), constructorArgType, readAccessor, target);
        }

    }

    /**
     * Apply the mapping to a single field.
     * @param simpleMapping
     * @param propertyName
     * @return 
     */
    private String applyMapping(Source simpleMapping, String propertyName) {
        if (simpleMapping == null || StringUtils.isEmpty(simpleMapping.value())) {
            return propertyName;
        } else {
            return simpleMapping.value();
        }
    }

    private void applyMergerSettings(String propertyName, HashMap<Class, Annotation> annotations, FieldMetadata metadata) {
        Source simpleMapping = (Source) annotations.get(Source.class);
        Sources compoundMapping = (Sources) annotations.get(Sources.class);
        SourceNames sourceNames = (SourceNames) annotations.get(SourceNames.class);

        if (simpleMapping != null && compoundMapping != null) {
            throw new IllegalStateException("Source and Sources annotation present at the same time");
        }

        if (sourceNames != null) {
            String[] sources = readSourceBeanNamesFromAnnotation(sourceNames);
            metadata.setSourceBeanNames(sources);
        }
        
        
        //add the single property value merger.
        if (simpleMapping != null && simpleMapping.merger() != null) {
            String sourceFieldName = StringUtils.isEmpty(simpleMapping.value()) ? propertyName : simpleMapping.value();
            metadata.setSinglePropertyValueMerger(sourceFieldName,
                    (Class)simpleMapping.merger(),
                    simpleMapping.mergerParam(),
                    simpleMapping.sourceBean(),
                    0);

            //the default merger is already set on the metadate for it's being pre-filled with default data.
            return;
        }

        //add the compound property value merger.
        if (compoundMapping != null && compoundMapping.merger() != null) {
            metadata.setPropertyValueMerger(compoundMapping.merger());
            metadata.setMergerParameter(compoundMapping.mergerParam());
            return;
        }
        
        //if no annottion, then the default values should be already set.
    }
    
    /**
     * read the source bean names from the annotation.
     * @param annotation
     * @return 
     */
    private String[] readSourceBeanNamesFromAnnotation(SourceNames annotation) {

        if (annotation == null || ArrayUtils.isEmpty(annotation.value())) {
            return new String[]{""};
        }

        return annotation.value();
    }

    @Override
    FieldMetadata buildFieldMetadata(int parameterIndex, Class parameterType, Annotation[] parameterAnnotations, Class beanClass) {
        //the parameter index is not quite useful here.
        String propertyName = "arg"+parameterIndex;
        
        //build the default metadata;
        FieldMetadata ret = buildDefaultFieldMetadata(propertyName);
        
        HashMap<Class, Annotation> annotations = new HashMap<Class, Annotation>();
        
        //map the annotations for simple use.
        for (Annotation annotation : parameterAnnotations) {
            annotations.put(annotation.annotationType(), annotation);
        }
        
        applySettingsToFieldMetadata(ret, propertyName, parameterType, null, annotations);
        applyMergerSettings(propertyName, annotations, ret);
        
        return ret;
    }

    @Override
    Constructor findAppropiateConstructor(Class beanClass) {
        Constructor[] beanConstructors = beanClass.getConstructors();
        
        for (Constructor constructor : beanConstructors) {
            if (constructor.isAnnotationPresent(DTOConstructor.class)) {
                return constructor;
            }
        }
        
        //return the last constructor.
        return beanConstructors[beanConstructors.length - 1];
    }
}
