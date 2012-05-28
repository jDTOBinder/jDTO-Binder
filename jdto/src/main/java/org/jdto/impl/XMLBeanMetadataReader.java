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

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jdto.impl.xml.DTOElement;
import org.jdto.impl.xml.DTOSourceField;
import org.jdto.impl.xml.DTOTargetConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains static methods to help reading the metadata of xml configuration objects.
 * @author Juan Alberto Lopez Cavallotti
 */
class XMLBeanMetadataReader {

    private static final Logger logger = LoggerFactory.getLogger(XMLBeanMetadataReader.class);

    /**
     * Read the default bean names from a DTO element.
     * @param dtoElement
     * @return 
     */
    static String[] readDefaultBeanNames(DTOElement dtoElement) {
        List<String> names = dtoElement.getBeanNames();

        //if no names are defined, then return an array with only the default bean.
        if (names == null || names.isEmpty()) {
            return new String[]{""}; //
        }

        //else convert the list to array of strings
        return names.toArray(new String[]{});
    }

    /**
     * Try to read the source fields, if no field is present, then use the same 
     * target property name.
     * @param config
     * @return 
     */
    static void readSourceFields(String propertyName, List<DTOSourceField> sources, FieldMetadata metadata) {

        //if no source field metadata then keep it default
        if (sources == null || sources.isEmpty()) {
            return;
        }

        //go through the source fields
        //first clear the source field list
        metadata.getSourceFields().clear();

        for (DTOSourceField sourceField : sources) {

            String sourceFieldName = sourceField.getName();

            //if the field name is empty, then the default is the property name.
            if (StringUtils.isEmpty(sourceFieldName)) {
                sourceFieldName = propertyName;
            }

            Class merger = StringUtils.isEmpty(sourceField.getMerger())
                    ? XMLBeanInspector.defaultSinglePropertyMerger()
                    : BeanClassUtils.safeGetClass(sourceField.getMerger());

            String[] mergerParam = StringUtils.isEmpty(sourceField.getMergerParam())
                    ? XMLBeanInspector.defaultMergerParameter()
                    : StringUtils.split(sourceField.getMergerParam(), ";");

            String sourceBeanName = StringUtils.isEmpty(sourceField.getSourceBean())
                    ? XMLBeanInspector.defaultSourceBeanName()
                    : sourceField.getSourceBean();

            //finally set the metadata
            metadata.getSourceFields().add(sourceFieldName);
            metadata.setSinglePropertyValueMerger(sourceFieldName, merger, mergerParam, sourceBeanName);
        }

    }

    static void readTargetFieldConfig(String propertyName, DTOTargetConfig config, FieldMetadata metadata) {


        Class merger = StringUtils.isEmpty(config.getMerger())
                ? XMLBeanInspector.defaultMultiPropertyMerger()
                : BeanClassUtils.safeGetClass(config.getMerger());

        String[] mergerParameter = StringUtils.isEmpty(config.getMergerParam())
                ? XMLBeanInspector.defaultMergerParameter()
                : StringUtils.split(config.getMergerParam(), ";");

        metadata.setPropertyValueMerger(merger);
        metadata.setMergerParameter(mergerParameter);
    }
}
