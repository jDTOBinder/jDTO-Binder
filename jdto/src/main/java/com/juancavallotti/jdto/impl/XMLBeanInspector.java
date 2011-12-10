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

package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.impl.xml.DTOElement;
import com.juancavallotti.jdto.impl.xml.DTOMappings;
import com.juancavallotti.jdto.impl.xml.DTOTargetField;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.StringUtils;

/**
 * WARNING: THIS CLASS IS NOT PART OF JDTO PUBLIC API
 * 
 * XML Configuration reader.
 * @author juancavallotti
 */
public class XMLBeanInspector extends AbstractBeanInspector implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Build the metadata for fields.
     * @param propertyName
     * @param readAccessor
     * @param beanClass
     * @return 
     */
    @Override
    FieldMetadata buildFieldMetadata(String propertyName, Method readAccessor, Class beanClass) {

        FieldMetadata metadata = buildDefaultFieldMetadata(propertyName);

        if (!targetFieldMappings.containsKey(beanClass.getName())) {
            logger.info("No settings for bean: " + beanClass.getName() + " using default values...");
            return metadata;
        }

        //build metadata from an xml file.
        DTOTargetField config = targetFieldMappings.get(beanClass.getName()).get(propertyName);

        //if no config, then apply default.
        if (config == null) {
            return metadata;
        }

        //the most basic check, the transient check.
        if (config.isDtoTransient()) {
            metadata.setFieldTransient(true);
            return metadata;
        }

        //read the source propertyies.
        XMLBeanMetadataReader.readSourceFields(propertyName, config, metadata);
        
        //read the target configuration.
        XMLBeanMetadataReader.readTargetFieldConfig(propertyName, config, metadata);
        
        //try to apply the cascade logic.
        if (config.isCascade()) {
            //then we'll need to apply the cascade logic.
            Class targetClass = StringUtils.isEmpty(config.getFieldType())
                    ? defaultCascadeTargetClass()
                    : BeanClassUtils.safeGetClass(config.getFieldType());

            applyCascadeLogic(targetClass, null, readAccessor, metadata);
        }

        return metadata;
    }

    /**
     * Read the source bean names for a given class.
     * @param beanClass
     * @return 
     */
    @Override
    String[] readSourceBeanNames(Class beanClass) {
        String beanClassName = beanClass.getName();

        //get the xml configuration
        DTOElement element = configuredDtos.get(beanClassName);

        if (element == null) {
            return defaultSourceBeanNames();
        }

        return XMLBeanMetadataReader.readDefaultBeanNames(element);
    }

    /**
     * Get an instance of XMLBeanInspector which reads configuration over a 
     * package resource.
     * @param packageResource
     * @return 
     */
    public static XMLBeanInspector getInstance(String packageResource) {
        InputStream is = XMLBeanInspector.class.getResourceAsStream(packageResource);
        try {

            if (is == null) {
                logger.error("wrong package resource: " + packageResource);
                throw new IllegalArgumentException("Can't find the package resource.");
            }

            return new XMLBeanInspector(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    logger.error("Got exception while parsing xml file", ex);
                    throw new RuntimeException(ex);
                }
            }
        }
    }
    private final DTOMappings mappings;
    //convenience map to access easily class and fields config.
    private final HashMap<String, DTOElement> configuredDtos;
    private final HashMap<String, HashMap<String, DTOTargetField>> targetFieldMappings;

    /**
     * Read the XML file which can be read by accessing the input stream taken 
     * as a parameter.
     * @param xmlStream 
     */
    public XMLBeanInspector(InputStream xmlStream) {
        if (xmlStream == null) {
            logger.error("Null xml input stream");
            throw new IllegalArgumentException("XML Stream cannot be null");
        }

        mappings = parseXML(xmlStream);
        configuredDtos = new HashMap<String, DTOElement>();
        targetFieldMappings = new HashMap<String, HashMap<String, DTOTargetField>>();

        //populate the target field mappings.
        for (DTOElement dto : mappings.getElements()) {

            HashMap<String, DTOTargetField> fieldsMapping = new HashMap<String, DTOTargetField>();

            for (DTOTargetField targetField : dto.getTargetFields()) {
                fieldsMapping.put(targetField.getFieldName(), targetField);
            }

            configuredDtos.put(dto.getType(), dto);
            targetFieldMappings.put(dto.getType(), fieldsMapping);
        }
    }

    /**
     * Perform the parsing of the XML File
     * @param xmlStream
     * @return 
     */
    private DTOMappings parseXML(InputStream xmlStream) {
        try {
            JAXBContext context = JAXBContext.newInstance(DTOMappings.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            DTOMappings inputMappings = (DTOMappings) unmarshaller.unmarshal(xmlStream);

            return inputMappings;
        } catch (Exception ex) {
            logger.error("Got exception while parsing xml file", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is not really part of the JDTO public API
     * DO NOT CALL IT
     * @return 
     */
    public DTOMappings getMappings() {
        return mappings;
    }

    /**
     * Build the bean metadata based on the information recovered on the xml on
     * an eager way!
     * parsing process
     * @return 
     */
    public synchronized HashMap<Class, BeanMetadata> buildMetadata() {
        HashMap<Class, BeanMetadata> ret = new HashMap<Class, BeanMetadata>();


        //for each configured dto, build its metadata.
        for (DTOElement dtoElement : mappings.getElements()) {
            Class dtoClass = BeanClassUtils.safeGetClass(dtoElement.getType());
            if (dtoClass == null) {
                logger.warn("could not find bean of type: " + dtoElement.getType() + " so i will ignore it!");
                continue;
            }

            BeanMetadata dtoMetadata = super.inspectBean(dtoClass);
            ret.put(dtoClass, dtoMetadata);
        }

        return ret;
    }

    @Override
    FieldMetadata buildFieldMetadata(int parameterIndex, Class parameterType, Annotation[] parameterAnnotations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    Constructor findAppropiateConstructor(Class beanClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
