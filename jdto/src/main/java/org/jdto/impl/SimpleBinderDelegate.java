/*
 *    Copyright 2012 Juan Alberto López Cavallotti
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

import org.jdto.BeanModifier;
import org.jdto.BeanModifierAware;
import org.jdto.MultiPropertyValueMerger;
import org.jdto.SinglePropertyValueMerger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for binding the DTOs for real.
 * @author Juan Alberto Lopez Cavallotti
 */
class SimpleBinderDelegate implements Serializable {

    private static final long serialVersionUID = 1L;
    private final DTOBinderBean binderBean;
    private AbstractBeanInspector inspector;
    private BeanModifier modifier;
    private static final Logger logger = LoggerFactory.getLogger(SimpleBinderDelegate.class);

    SimpleBinderDelegate(DTOBinderBean binderBean) {
        this.binderBean = binderBean;
    }

     <T> T bindFromBusinessObject(HashMap<Class, BeanMetadata> metadataMap, Class<T> dtoClass, Object... businessObjects) {
        //first of all, we try to find the metadata for the DTO to build, if not
        //then we build it.
        BeanMetadata metadata = findBeanMetadata(metadataMap, dtoClass);

        //the returned object.
        T ret = null;

        //the immutable constructor args list
        ArrayList immutableConstructorArgs = null;

        //build the appropiate object.
        if (metadata.isImmutableBean()) {
            immutableConstructorArgs = new ArrayList();
        } else {
            ret = BeanClassUtils.createInstance(dtoClass);
        }



        HashMap<String, FieldMetadata> propertyMappings = metadata.getFieldMetadata();

        //the map of source beans by name
        HashMap<String, Object> sourceBeans = new HashMap<String, Object>();

        if (metadata.isImmutableBean()) {
            for (FieldMetadata fieldMetadata : metadata.getConstructorArgs()) {
                Object targetValue = buildTargetValue(metadata, fieldMetadata, ret, sourceBeans, businessObjects);
                
                //if the source and target types are not compatible, then apply the compatibility logic
                targetValue = applyCompatibilityLogic(fieldMetadata, targetValue);
                
                //if the object is immutable, then we need to store the value.
                immutableConstructorArgs.add(targetValue);
            }
        } else {
            //iterate through the properties and read the values from the business objects.
            for (String targetProperty : propertyMappings.keySet()) {
                //get the configuration for the DTO objects.
                FieldMetadata fieldMetadata = propertyMappings.get(targetProperty);
                Object targetValue = buildTargetValue(metadata, fieldMetadata, ret, sourceBeans, businessObjects);
                
                //if the source and target types are not compatible, then apply the compatibility logic
                targetValue = applyCompatibilityLogic(fieldMetadata, targetValue);
                
                modifier.writePropertyValue(targetProperty, targetValue, ret);
            }
        }

        //finally, if the bean is immutable, build the instance with the values!
        if (metadata.isImmutableBean()) {
            ret = BeanClassUtils.createInstance(dtoClass, metadata.getImmutableConstructor(), immutableConstructorArgs);
        }

        return ret;
    }

    private <T> Object buildTargetValue(BeanMetadata metadata, FieldMetadata fieldMetadata, T ret, HashMap<String, Object> sourceBeans, Object... businessObjects) {
        //create a buffer for the source values.
        List<Object> sourceValues = new ArrayList<Object>();

        //get a list of the properties to read for this DTO
        List<String> sourceProperties = fieldMetadata.getSourceFields();



        //read all the values.
        for (String sourceProperty : sourceProperties) {

            populateSourceBeans(sourceBeans, metadata, fieldMetadata, businessObjects);

            Object finalValue = applyMergeToSingleField(sourceBeans, sourceProperty, fieldMetadata);
            sourceValues.add(finalValue);
        }


        //merge the values into one
        MultiPropertyValueMerger merger = fieldMetadata.getPropertyValueMerger();

        Object targetValue = null;

        if (fieldMetadata.isCascadePresent()) {
            targetValue = applyCascadeLogic(sourceValues, fieldMetadata);
        } else {
            injectContextIfNeeded(merger);
            targetValue = merger.mergeObjects(sourceValues, fieldMetadata.getMergerParameter());
        }

        //return the value
        return targetValue;
    }

    /**
     * Create the appropiate metadata if it doesn't exist.
     * @param <T>
     * @param metadataMap
     * @param dtoClass
     * @return 
     */
    private <T> BeanMetadata findBeanMetadata(HashMap<Class, BeanMetadata> metadataMap, Class<T> dtoClass) {

        BeanMetadata ret = metadataMap.get(dtoClass);

        if (ret == null) {
            ret = inspector.inspectBean(dtoClass);
        }

        metadataMap.put(dtoClass, ret);

        return ret;
    }

    /**
     * Apply the cascade logic for generating the target value.
     * @param sourceValues
     * @param fieldMetadata
     * @return 
     */
    private Object applyCascadeLogic(List<Object> sourceValues, FieldMetadata fieldMetadata) {

        Object[] values = sourceValues.toArray();

        Object ret = null;
        List[] listValues = null;

        if (fieldMetadata.getCascadeTargetClass() == null || fieldMetadata.getCascadeTargetClass() == Object.class) {
            throw new IllegalStateException("Could not infer correctly the type of cascaded dto");
        }

        switch (fieldMetadata.getCascadeType()) {
            case SINGLE:
                ret = binderBean.bindFromBusinessObject(fieldMetadata.getCascadeTargetClass(), values);
                break;
            case COLLECTION:
                //pretty dangerous cast I say :)
                listValues = new List[values.length];

                for (int i = 0; i < values.length; i++) {
                    listValues[i] = (List) convertValueToList(values[i]);
                }

                ret = binderBean.bindFromBusinessObjectList(fieldMetadata.getCascadeTargetClass(), listValues);
                break;
            case ARRAY:
                //pretty dangerous cast I say :)
                listValues = new List[values.length];

                for (int i = 0; i < values.length; i++) {
                    listValues[i] = (List) convertValueToList(values[i]);
                }

                List retList = binderBean.bindFromBusinessObjectList(fieldMetadata.getCascadeTargetClass(), listValues);
                ret = retList.toArray();
                break;
            default:
                break;
        }

        return ret;
    }

    private List convertValueToList(Object value) {

        if (value instanceof List) {
            return (List) value;
        }

        if (value instanceof Collection) {
            return new ArrayList((Collection) value);
        }

        if (value.getClass().isArray()) {
            return Arrays.asList((Object[]) value);
        }

        return null;
    }

    //***************** REVERSE PROCESS ************************//
    /**
     * This is the modest reverse process. Not thaat useful but stay tuned :)
     * @param <T>
     * @param metadataMap
     * @param entityClass
     * @param dto
     * @return 
     */
     <T> T extractFromDto(HashMap metadataMap, Class<T> entityClass, Object dto) {
        BeanMetadata metadata = findBeanMetadata(metadataMap, dto.getClass());

        T ret = BeanClassUtils.createInstance(entityClass);

        HashMap<String, FieldMetadata> mappings = metadata.getFieldMetadata();

        for (String source : mappings.keySet()) {

            FieldMetadata fieldMetadata = mappings.get(source);

            //as documented we do not support dissasembling compound properties.
            if (fieldMetadata.getSourceFields().size() != 1) {
                continue;
            }

            //we currently dont support field uncascading
            //but we might on the future.
            if (fieldMetadata.isCascadePresent()) {
                continue;
            }

            //this is the only possibility left.
            String target = fieldMetadata.getSourceFields().get(0);

            //no need to unmerge
            //so we're ready to copy!
            Object value = modifier.readPropertyValue(source, dto);

            //and set
            modifier.writePropertyValue(target, value, ret);
        }


        return ret;
    }

    private Object applyMergeToSingleField(HashMap<String, Object> sourceBeans, String sourceProperty, FieldMetadata fieldMetadata) {
        SinglePropertyValueMerger merger = fieldMetadata.getSourceMergers().get(sourceProperty);
        String[] mergerExtraParam = fieldMetadata.getSourceMergersParams().get(sourceProperty);
        String sourceBean = fieldMetadata.getSourceBeans().get(sourceProperty);

        Object bo = sourceBeans.get(sourceBean);

        if (bo == null) {
            throw new IllegalStateException("could not find source bean with name: " + sourceBean);
        }

        Object sourceValue = modifier.readPropertyValue(sourceProperty, bo);

        injectContextIfNeeded(merger);
        return merger.mergeObjects(sourceValue, mergerExtraParam);
    }

    //GETTERS AND SETTERS
    public AbstractBeanInspector getInspector() {
        return inspector;
    }

    public void setInspector(AbstractBeanInspector inspector) {
        this.inspector = inspector;
    }

    public BeanModifier getModifier() {
        return modifier;
    }

    public void setModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }

    private void populateSourceBeans(HashMap<String, Object> sourceBeans, BeanMetadata metadata, FieldMetadata fieldMetadata, Object[] bos) {

        //clear the mappings
        sourceBeans.clear();

        //populate, by default the mapping on the field or else, the one on the ben.
        String[] names = (ArrayUtils.isEmpty(fieldMetadata.getSourceBeanNames()))
                ? metadata.getDefaultBeanNames() : fieldMetadata.getSourceBeanNames();

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Object bean = bos[i];
            sourceBeans.put(name, bean);
        }

        //add the default bean name.
        sourceBeans.put("", bos[0]);
    }

    private void injectContextIfNeeded(Object target) {

        if (target instanceof BeanModifierAware) {
            BeanModifierAware bma = (BeanModifierAware) target;
            bma.setBeanModifier(modifier);
        }

    }

    private Object applyCompatibilityLogic(FieldMetadata fieldMetadata, Object targetValue) {
        
        if (targetValue == null) {
            return null;
        }
        
        //check if the types are compatible if so, then leave them alone
        if (fieldMetadata.getTargetType().isAssignableFrom(targetValue.getClass())) {
            return targetValue;
        }
        
        return ValueConversionHelper.compatibilize(targetValue, fieldMetadata.getTargetType());
    }
}
