package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.annotation.DTOCascade;
import com.juancavallotti.jdto.annotation.DTOTransient;
import com.juancavallotti.jdto.annotation.Source;
import com.juancavallotti.jdto.annotation.SourceNames;
import com.juancavallotti.jdto.annotation.Sources;
import com.juancavallotti.jdto.mergers.FirstObjectPropertyValueMerger;
import com.juancavallotti.jdto.mergers.IdentityPropertyValueMerger;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class in charge of building the appropiate bean metadata by analyzing a bean
 * rather than reading the configuration from an XML file. This bean will 
 * build metadata based on standard configurations and annotations.
 * @author juancavallotti
 */
class BeanInspector implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BeanInspector.class);

    protected <T> BeanMetadata inspectBean(Class<T> beanClass) {

        logger.debug("Strarting analysis of " + beanClass.toString());
        try {

            BeanMetadata ret = new BeanMetadata();

            HashMap<Field, Method> beanFields = BeanPropertyUtils.inspectClassFields(beanClass);

            for (Field field : beanFields.keySet()) {

                Method getter = beanFields.get(field);

                String beanProperty = field.getName();

                HashMap<Class, Annotation> annotations = readAnnotations(field, getter);

                FieldMetadata metadata = new FieldMetadata();

                buildSourceProperties(field, getter, annotations, metadata);


                if (metadata.isFieldTransient()) {
                    //nothing to bind, this could happen if the field is tansient
                    //or not found.
                    continue;
                }

                logger.info("\tBound " + beanProperty + " to " + metadata.getSourceFields().toString());

                //add the merger for the whole process
                buildMerger(field, getter, annotations, metadata);

                ret.putFieldMetadata(beanProperty, metadata);
            }
            
            
            String[] sourceBeanNames = readSourceBeanNames(beanClass.getAnnotation(SourceNames.class));
            
            ret.setDefaultBeanNames(sourceBeanNames);
            
            return ret;

        } catch (Exception ex) {
            logger.error("Error while inspecting bean" + beanClass.toString(), ex);
            throw new RuntimeException("Could not inspect bean", ex);
        }
    }

    private void buildSourceProperties(Field field, Method getter, HashMap<Class, Annotation> annotations, FieldMetadata target) {
        //the default behavior is to bound by attribute name.
        List<String> sources = new LinkedList<String>();
        
        //check if the field is DTOTransient
        if (annotations.containsKey(DTOTransient.class)) {
            target.setFieldTransient(true);
            return;
        }

        //try with a simple mapping.
        Source simpleMapping = (Source) annotations.get(Source.class);
        Sources compoundMapping = (Sources) annotations.get(Sources.class);
        DTOCascade cascade = (DTOCascade) annotations.get(DTOCascade.class);

        if (simpleMapping != null && compoundMapping != null) {
            throw new IllegalStateException("Source and Sources annotation present at the same time");
        }

        if (simpleMapping != null) {
            String sourceField = applyMapping(simpleMapping, field);
            sources.add(sourceField);
        }

        if (compoundMapping != null) {
            for (Source source : compoundMapping.value()) {
                String sourceField = applyMapping(source, field);
                sources.add(sourceField);
                target.addSinglePropertyValueMerger(sourceField, 
                        InstancePool.getOrCreate(source.merger()), 
                        source.mergerParam(),
                        source.sourceBean());
            }
        }

        if (cascade != null) {
            applyCascadeLogic(cascade, field, getter, target);
        }

        //if no annotations, we should add the same field name
        if (simpleMapping == null && compoundMapping == null) {
            sources.add(field.getName());
        }
        target.setSourceFields(sources);

    }

    private void buildMerger(Field field, Method getter, HashMap<Class, Annotation> annotations, FieldMetadata metadata) {
        Source simpleMapping = (Source) annotations.get(Source.class);
        Sources compoundMapping = (Sources) annotations.get(Sources.class);
        SourceNames sourceNames = (SourceNames) annotations.get(SourceNames.class);
        
        if (simpleMapping != null && compoundMapping != null) {
            throw new IllegalStateException("Source and Sources annotation present at the same time");
        }
        
        if (sourceNames != null) {
            String[] sources = readSourceBeanNames(sourceNames);
            metadata.setSourceBeanNames(sources);
        }
        
        //add the single property value merger.
        if (simpleMapping != null && simpleMapping.merger() != null) {
            String sourceFieldName = StringUtils.isEmpty(simpleMapping.value()) ? field.getName() : simpleMapping.value();
            metadata.addSinglePropertyValueMerger(sourceFieldName, 
                    InstancePool.getOrCreate(simpleMapping.merger()), 
                    simpleMapping.mergerParam(),
                    simpleMapping.sourceBean());
            
            //the default merger for the gobal field.
            metadata.setPropertyValueMerger(InstancePool.getOrCreate(FirstObjectPropertyValueMerger.class));
            metadata.setMergerParameter("");
            return;
        }

        //add the compound property valur merger.
        if (compoundMapping != null && compoundMapping.merger() != null) {
            metadata.setPropertyValueMerger(InstancePool.getOrCreate(compoundMapping.merger()));
            metadata.setMergerParameter(compoundMapping.mergerParam());
            return;
        }

        //the default behavior if no annotation is present.
        metadata.setPropertyValueMerger(InstancePool.getOrCreate(FirstObjectPropertyValueMerger.class));
        metadata.setMergerParameter("");
        //add the default merger for the property.
        metadata.addSinglePropertyValueMerger(field.getName(), InstancePool.getOrCreate(IdentityPropertyValueMerger.class), "", "");
    }

    private String applyMapping(Source simpleMapping, Field field) {
        if (simpleMapping == null || StringUtils.isEmpty(simpleMapping.value())) {
            return field.getName();
        } else {
            return simpleMapping.value();
        }
    }

    private HashMap<Class, Annotation> readAnnotations(Field field, Method getter) {
        HashMap<Class, Annotation> ret = new HashMap<Class, Annotation>();

        //read the transient annotation.
        populateAnnotation(DTOTransient.class, field, getter, ret);

        //read the simple mapping annotation
        populateAnnotation(Source.class, field, getter, ret);

        //read the compound mapping annotation
        populateAnnotation(Sources.class, field, getter, ret);

        //read the cascade annotation.
        populateAnnotation(DTOCascade.class, field, getter, ret);
        
        //the source names for a compound field.
        populateAnnotation(SourceNames.class, field, getter, ret);
        
        return ret;
    }

    private <T extends Annotation> void populateAnnotation(Class<T> aClass, Field field, Method getter, HashMap<Class, Annotation> annotations) {
        T annotationInstance = BeanPropertyUtils.getAnnotation(aClass, field, getter);
        if (annotationInstance != null) {
            annotations.put(aClass, annotationInstance);
        }
    }

    /**
     * Applies the cascade logic to the field metadata.
     * @param cascade
     * @param field
     * @param ter
     * @param target 
     */
    private void applyCascadeLogic(DTOCascade cascade, Field field, Method getter, FieldMetadata target) {

        target.setCascadePresent(true);

        CascadeType cascadeType = null;

        if (List.class.isAssignableFrom(field.getType())) {
            cascadeType = CascadeType.COLLECTION;
        } else if (field.getType().isArray()) {
            cascadeType = CascadeType.ARRAY;
        } else {
            cascadeType = CascadeType.SINGLE;
        }

        //if the target type is pressent on the annotation, then all is quite simple
        if (cascade.targetType() != null && cascade.targetType() != Object.class) {
            target.setCascadeTargetClass(cascade.targetType());
        } else { //if not, then inferit by the declaration on the dto.
            Class targetType = inferTypeOfProperty(field, getter, cascadeType);
            target.setCascadeTargetClass(targetType);
        }

        target.setCascadeType(cascadeType);
    }

    /**
     * Try to infer the type of a property, if the property is a collection, then
     * try to infer generic type.
     * @param field
     * @param ter
     * @param cascadeType
     * @return 
     */
    private Class inferTypeOfProperty(Field field, Method getter, CascadeType cascadeType) {

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

    private String[] readSourceBeanNames(SourceNames annotation) {
        
        if (annotation == null || ArrayUtils.isEmpty(annotation.value())) {
            return new String[] {""};
        }
        
        return annotation.value();
    }
}
