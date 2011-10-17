package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import com.juancavallotti.jdto.impl.xml.DTOElement;
import com.juancavallotti.jdto.impl.xml.DTOSourceField;
import com.juancavallotti.jdto.impl.xml.DTOTargetField;
import com.juancavallotti.jdto.mergers.FirstObjectPropertyValueMerger;
import com.juancavallotti.jdto.mergers.IdentityPropertyValueMerger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;

/**
 * Contains static methods to help reading the metadata of xml configuration objects.
 * @author juancavallotti
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
     * Try to find a class out of a string or return null;
     * @param type
     * @return 
     */
    static Class safeGetClass(String type) {
        //if no type then no class :D
        if (StringUtils.isEmpty(type)) {
            return null;
        }

        try {
            return Class.forName(type);
        } catch (Exception ex) {
            logger.error("Error while trying to read the dto class", ex);
            return null;
        }
    }

    /**
     * Read and build the metadata for a single field, taking, as needed, the 
     * default values.
     * @param dtoClass
     * @param dtoTargetField
     * @return 
     * @throws Any reflection-related exception
     */
    static FieldMetadata readFieldMetadata(Class dtoClass, DTOTargetField dtoTargetField) {
        FieldMetadata ret = new FieldMetadata();

        //the cascade flag
        ret.setCascadePresent(dtoTargetField.isCascade());

        //the transient flag
        ret.setFieldTransient(dtoTargetField.isDtoTransient());

        //find the getter for this bean
        Method getter = BeanPropertyUtils.findGetterMethod(dtoTargetField.getFieldName(), dtoClass);

        //sadly, we depend on the fields being declared, this will improve on the future
        Field field = BeanPropertyUtils.readSafeField(dtoClass, dtoTargetField.getFieldName());

        //apply the cascade logic
        if (ret.isCascadePresent()) {
            Class cascadeTargerClass = safeGetClass(dtoTargetField.getFieldType());
            applyCascadeLogic(cascadeTargerClass, field, getter, ret);
        }

        //set the merger parameter.
        String mergerParam = dtoTargetField.getMergerParam();

        if (StringUtils.isEmpty(mergerParam)) {
            mergerParam = ""; //the default value
        }

        ret.setMergerParameter(mergerParam);

        String mergerClassName = dtoTargetField.getMerger();
        Class mergerClass = null;

        if (StringUtils.isEmpty(mergerClassName)) {
            mergerClass = FirstObjectPropertyValueMerger.class;
        } else {
            mergerClass = safeGetClass(mergerClassName);
            if (mergerClass == null) {
                logger.warn("Could not find merger of class: " + mergerClassName + " using default");
                mergerClass = FirstObjectPropertyValueMerger.class;
            }
        }

        //set the value merger
        ret.setPropertyValueMerger((MultiPropertyValueMerger) InstancePool.getOrCreate(mergerClass));

        configureSourceFields(ret, dtoTargetField);

        return ret;
    }

    /**
     * Applies the cascade logic to the field metadata.
     * COPIED FROM BEAN INSPECTOR - //TODO UNIFY THIS INTO JUST ONE METHOD
     * @param cascade
     * @param field
     * @param ter
     * @param target 
     */
    private static void applyCascadeLogic(Class cascadeTargetType, Field field, Method getter, FieldMetadata target) {

        CascadeType cascadeType = null;

        if (List.class.isAssignableFrom(getter.getReturnType())) {
            cascadeType = CascadeType.COLLECTION;
        } else if (getter.getReturnType().isArray()) {
            cascadeType = CascadeType.ARRAY;
        } else {
            cascadeType = CascadeType.SINGLE;
        }

        //if the target type is pressent on the annotation, then all is quite simple
        if (cascadeTargetType != null && cascadeTargetType != Object.class) {
            target.setCascadeTargetClass(cascadeTargetType);
        } else { //if not, then inferit by the declaration on the dto.
            Class targetType = inferTypeOfProperty(field, getter, cascadeType);
            target.setCascadeTargetClass(targetType);
        }

        target.setCascadeType(cascadeType);
    }

    /**
     * Try to infer the type of a property, if the property is a collection, then
     * try to infer generic type. <br />
     * COPIED FROM BEAN INSPECTOR - //TODO UNIFY THIS INTO JUST ONE METHOD
     * @param field
     * @param ter
     * @param cascadeType
     * @return 
     */
    private static Class inferTypeOfProperty(Field field, Method getter, CascadeType cascadeType) {

        switch (cascadeType) {
            case SINGLE:
                return field.getType();
            case ARRAY:
                return field.getType().getComponentType();
            case COLLECTION:
                return BeanPropertyUtils.getGenericType(field, getter);
        }
        return null;
    }

    /**
     * Configure the source fields of a XML configured target field
     * @param ret
     * @param dtoTargetField 
     */
    private static void configureSourceFields(FieldMetadata ret, DTOTargetField dtoTargetField) {
        List<DTOSourceField> sourceFields = dtoTargetField.getSources();

        List<String> sourceFieldNames = new LinkedList<String>();
        HashMap<String, SinglePropertyValueMerger> mergers = new HashMap<String, SinglePropertyValueMerger>();
        HashMap<String, String> mergerParams = new HashMap<String, String>();

        //no fields configured here, assume default behavior.
        if (sourceFields == null || sourceFields.isEmpty()) {
            ret.setSourceFields(Arrays.asList(dtoTargetField.getFieldName()));
            //the default merger
            mergers.put(dtoTargetField.getFieldName(), InstancePool.getOrCreate(IdentityPropertyValueMerger.class));
            ret.setSourceMergers(mergers);
            //the merger params
            mergerParams.put(dtoTargetField.getFieldName(), "");
            ret.setSourceMergersParams(mergerParams);
            return;
        }

        //otherwise, get the configured stuff
        for (DTOSourceField sourceField : sourceFields) {

            String fieldName = sourceField.getName();
            if (StringUtils.isEmpty(fieldName)) {
                fieldName = dtoTargetField.getFieldName();
            }

            sourceFieldNames.add(fieldName);

            Class mergerClass = safeGetClass(sourceField.getMerger());

            if (mergerClass == null) {
                mergers.put(fieldName, InstancePool.getOrCreate(IdentityPropertyValueMerger.class));
            } else {
                mergers.put(fieldName, (SinglePropertyValueMerger) InstancePool.getOrCreate(mergerClass));
            }

            //the merger param

            String mergerParam = sourceField.getMergerParam();

            if (StringUtils.isEmpty(mergerParam)) {
                mergerParam = null;
            }

            mergerParams.put(fieldName, mergerParam);
        }

        ret.setSourceFields(sourceFieldNames);
        ret.setSourceMergers(mergers);
        ret.setSourceMergersParams(mergerParams);
    }

    /**
     * Try to read the source fields, if no field is present, then use the same 
     * target property name.
     * @param config
     * @return 
     */
    static void readSourceFields(String propertyName, DTOTargetField config, FieldMetadata metadata) {

        //if no source field metadata then keep it default
        if (config.getSources() == null || config.getSources().isEmpty()) {
            return;
        }

        //go through the source fields
        //first clear the source field list
        metadata.getSourceFields().clear();

        for (DTOSourceField sourceField : config.getSources()) {

            String sourceFieldName = sourceField.getName();

            //if the field name is empty, then the default is the property name.
            if (StringUtils.isEmpty(sourceFieldName)) {
                sourceFieldName = propertyName;
            }

            SinglePropertyValueMerger merger = (SinglePropertyValueMerger) (StringUtils.isEmpty(sourceField.getMerger())
                    ? XMLBeanInspector.defaultSinglePropertyMerger()
                    : InstancePool.getOrCreate(safeGetClass(sourceField.getMerger())));

            String mergerParam = StringUtils.isEmpty(sourceField.getMergerParam())
                    ? XMLBeanInspector.defaultMergerParameter()
                    : sourceField.getMergerParam();

            String sourceBeanName = StringUtils.isEmpty(sourceField.getSourceBean())
                    ? XMLBeanInspector.defaultSourceBeanName()
                    : sourceField.getSourceBean();
            
            //finally set the metadata
            metadata.getSourceFields().add(sourceFieldName);
            metadata.setSinglePropertyValueMerger(sourceFieldName, merger, mergerParam, sourceBeanName);
        }

    }
}
