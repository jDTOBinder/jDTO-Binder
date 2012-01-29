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

import com.juancavallotti.jdto.impl.xml.DTOConstructorArg;
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
     * This is the default package resource for the xml config file to be named. <br />
     * By convention, a bean inspector instance may look for this package resource
     * if no other location is provided. This serves the purpose of encouraging
     * convention-over-configuration.
     */
    public static final String DEFAULT_PACKAGE_RESOURCE = "/jdto-mappings.xml";

    /**
     * Build the metadata for fields.
     * @param propertyName
     * @param readAccessor
     * @param beanClass
     * @return the field metadata for the given property.
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
        XMLBeanMetadataReader.readSourceFields(propertyName, config.getSources(), metadata);

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
     * @return an array with the list of source bean names for the given class.
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
     * Build an instance of XML bean inspector by reading the configuration file
     * in the default location, see {@link XMLBeanInspector#DEFAULT_PACKAGE_RESOURCE}
     * @return an instance of the bean inspector or null if no default configuration found.
     */
    public static XMLBeanInspector getInstance() {
        InputStream is = XMLBeanInspector.class.getResourceAsStream(DEFAULT_PACKAGE_RESOURCE);
        try {
            if (is == null) {
                logger.info("Package resource " + DEFAULT_PACKAGE_RESOURCE + " is not present.");
                return null;
            }
            return new XMLBeanInspector(is);
        } catch (Exception ex) {
            logger.error("Got exception while trying to read the default xml configuration file", ex);
            return null;
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

    /**
     * Get an instance of XMLBeanInspector which reads configuration over a 
     * package resource.
     * @param packageResource
     * @return an instance of this XML inspector configured with the file located
     * in the packageResource path.
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
    private final HashMap<String, DTOConstructorArg[]> constrcutrorArgMappings;

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
        constrcutrorArgMappings = new HashMap<String, DTOConstructorArg[]>();
        
        //mappings can be null for pefectly valid xml files so we check that
        if (mappings.getElements() == null) {
            //nothing happens.
            return;
        }
        
        //populate the target field mappings.
        for (DTOElement dto : mappings.getElements()) {

            HashMap<String, DTOTargetField> fieldsMapping = new HashMap<String, DTOTargetField>();

            //if the constructor is immutable this may cause problems.
            if (dto.getTargetFields() != null) {
                for (DTOTargetField targetField : dto.getTargetFields()) {
                    fieldsMapping.put(targetField.getFieldName(), targetField);
                }
            }

            configuredDtos.put(dto.getType(), dto);
            targetFieldMappings.put(dto.getType(), fieldsMapping);

            if (dto.getConstructorArgs() == null) {
                continue;
            }

            DTOConstructorArg[] args = new DTOConstructorArg[dto.getConstructorArgs().size()];

            //read the constructor args
            for (int i = 0; i < dto.getConstructorArgs().size(); i++) {
                DTOConstructorArg arg = dto.getConstructorArgs().get(i);
                if (arg.getOrder() != null) {
                    //if the argument has a specific order, then place it.
                    args[arg.getOrder()] = arg;
                } else {
                    //otherwise use the declaration order.
                    args[i] = arg;
                }
            }

            constrcutrorArgMappings.put(dto.getType(), args);
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
     * @return the mappings object parsed from the configuration file.
     */
    public DTOMappings getMappings() {
        return mappings;
    }

    /**
     * Build the bean metadata based on the information recovered on the xml on
     * an eager way!
     * parsing process
     * @return the binding metadata generated out of the XML configuration file.
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
    FieldMetadata buildFieldMetadata(int parameterIndex, Class parameterType, Annotation[] parameterAnnotations, Class beanClass) {
        String propertyName = "arg" + parameterIndex;

        FieldMetadata metadata = buildDefaultFieldMetadata(propertyName);

        if (!constrcutrorArgMappings.containsKey(beanClass.getName())) {
            logger.info("No settings for arg: " + parameterIndex + " of bean: " + beanClass.getName() + " using default values...");
            return metadata;
        }

        //build metadata from an xml file.
        DTOConstructorArg arg = constrcutrorArgMappings.get(beanClass.getName())[parameterIndex];

        //if no config, then apply default.
        if (arg == null) {
            throw new RuntimeException("No settings for constructor arg " + parameterIndex + " of bean " + beanClass.getName());
        }

        //read the source propertyies.
        XMLBeanMetadataReader.readSourceFields(propertyName, arg.getSources(), metadata);

        //read the target configuration.
        XMLBeanMetadataReader.readTargetFieldConfig(propertyName, arg, metadata);

        //try to apply the cascade logic.
        if (arg.isCascade()) {
            //then we'll need to apply the cascade logic.
            Class targetClass = StringUtils.isEmpty(arg.getFieldType())
                    ? defaultCascadeTargetClass()
                    : BeanClassUtils.safeGetClass(arg.getFieldType());

            applyCascadeLogic(targetClass, parameterType, null, metadata);
        }

        return metadata;
    }

    /**
     * Find a constructor with the appropiate types.
     * @param beanClass
     * @return the constructor needed to build an instance of this class.
     */
    @Override
    Constructor findAppropiateConstructor(Class beanClass) {
        DTOConstructorArg[] args = constrcutrorArgMappings.get(beanClass.getName());

        Class[] types = new Class[args.length];

        for (int i = 0; i < types.length; i++) {
            types[i] = BeanClassUtils.safeGetClass(args[i].getType());
        }

        return BeanClassUtils.safeGetConstructor(beanClass, types);
    }
}
