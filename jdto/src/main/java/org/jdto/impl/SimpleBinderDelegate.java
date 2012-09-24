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

import java.io.Serializable;
import java.util.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jdto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for binding the DTOs for real.
 *
 * @author Juan Alberto Lopez Cavallotti
 */
class SimpleBinderDelegate implements Serializable {

    private static final long serialVersionUID = 1L;
    private final DTOBinderBean binderBean;
    private AbstractBeanInspector inspector;
    private BeanModifier modifier;
    private PropertyValueMergerInstanceManager mergerManager;
    private static final Logger logger = LoggerFactory.getLogger(SimpleBinderDelegate.class);

    SimpleBinderDelegate(DTOBinderBean binderBean) {
        this.binderBean = binderBean;
    }

    <T> T bindFromBusinessObject(HashMap<Class, BeanMetadata> metadataMap, Class<T> dtoClass, Object... businessObjects) {
        //first of all, we try to find the metadata for the DTO to build, if not
        //then we build it.
        BeanMetadata metadata = findBeanMetadata(metadataMap, dtoClass);
        
        DTOCacheKey cacheKey = new DTOCacheKey(metadata, businessObjects);
        
        //check if the object to return is cached, this enhances performance and 
        //helps to mitigate the DTOCascade cycle problem.
        
        T ret = (T) binderBean.bindingContext.get().get(cacheKey);
        
        if (ret != null) {
            return ret;
        }
        
        //the immutable constructor args list
        ArrayList immutableConstructorArgs = null;

        //build the appropiate object.
        if (metadata.isImmutableBean()) {
            immutableConstructorArgs = new ArrayList();
        } else {
            ret = BeanClassUtils.createInstance(dtoClass);
            binderBean.bindingContext.get().put(cacheKey, ret);
        }



        HashMap<String, FieldMetadata> propertyMappings = metadata.getFieldMetadata();

        //the map of source beans by name
        HashMap<String, Object> sourceBeans = new HashMap<String, Object>();

        if (metadata.isImmutableBean()) {
            for (FieldMetadata fieldMetadata : metadata.getConstructorArgs()) {
                Object targetValue = buildTargetValue(metadata, fieldMetadata, ret, sourceBeans, businessObjects);

                //if the source and target types are not compatible, then apply the compatibility logic
                targetValue = ValueConversionHelper.applyCompatibilityLogic(fieldMetadata.getTargetType(), targetValue);

                //if the object is immutable, then we need to store the value.
                immutableConstructorArgs.add(targetValue);
            }
            ret = BeanClassUtils.createInstance(dtoClass, metadata.getImmutableConstructor(), immutableConstructorArgs);
            binderBean.bindingContext.get().put(cacheKey, ret);
        } else {
            //iterate through the properties and read the values from the business objects.
            for (String targetProperty : propertyMappings.keySet()) {
                //get the configuration for the DTO objects.
                FieldMetadata fieldMetadata = propertyMappings.get(targetProperty);
                Object targetValue = buildTargetValue(metadata, fieldMetadata, ret, sourceBeans, businessObjects);
                modifier.writePropertyValue(targetProperty, targetValue, ret);
            }
        }
        return ret;
    }

    private <T> Object buildTargetValue(BeanMetadata metadata, FieldMetadata fieldMetadata, T ret, HashMap<String, Object> sourceBeans, Object... businessObjects) {
        //create a buffer for the source values.
        List<Object> sourceValues = new ArrayList<Object>();

        //get a list of the properties to read for this DTO
        
        List<String> sourceProperties = fieldMetadata.getSourceFields();


        //field index to resolve collisions between property names
        int sourcePropertyIndex = 0;
        //read all the values.
        for (String sourceProperty : sourceProperties) {
            
            populateSourceBeans(sourceBeans, metadata, fieldMetadata, businessObjects);

            Object finalValue = applyMergeToSingleField(sourceBeans, sourceProperty, fieldMetadata, sourcePropertyIndex);
            sourceValues.add(finalValue);
            sourcePropertyIndex++;
        }


        //merge the values into one
        Class mergerClass = fieldMetadata.getPropertyValueMerger();

        Object targetValue = null;

        if (fieldMetadata.isCascadePresent()) {
            targetValue = applyCascadeLogic(sourceValues, fieldMetadata);
        } else {
            MultiPropertyValueMerger merger = (MultiPropertyValueMerger) mergerManager.getPropertyValueMerger(mergerClass); //todo get the merger from the context.
            targetValue = merger.mergeObjects(sourceValues, fieldMetadata.getMergerParameter());
        }

        //return the value
        return targetValue;
    }

    /**
     * Create the appropiate metadata if it doesn't exist.
     *
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
     *
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
     * This is the modest reverse process. Not that useful but stay tuned :)
     *
     * @param <T>
     * @param metadataMap
     * @param entityClass
     * @param dto
     * @return
     */
    <T> T extractFromDto(HashMap metadataMap, Class<T> entityClass, Object dto) {
        
        if (dto == null) {
            return null; //this satisfies f(null) = null
        }
        
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
            
            //if the source was the root object, then it's safer to ignore it.
            if (StringUtils.equals(Binding.ROOT_OBJECT, target)) {
                continue;
            }
            
            //read the source value
            Object value = modifier.readPropertyValue(source, dto);
            
            //try to restore the value
            value = applyRestoreToSingleField(value, fieldMetadata, 0);
            
            //and set
            modifier.writePropertyValue(target, value, ret);
        }


        return ret;
    }

    private Object applyMergeToSingleField(HashMap<String, Object> sourceBeans, String sourceProperty, FieldMetadata fieldMetadata, int fieldIndex) {

        //read the source value
        Object sourceValue = readSourceValue(sourceBeans, sourceProperty, fieldMetadata, fieldIndex);
        
        //read the merger information
        Class mergerClass = fieldMetadata.getSourceMergers()[fieldIndex];
        String[] mergerExtraParam = fieldMetadata.getSourceMergersParams()[fieldIndex];

        SinglePropertyValueMerger merger = (SinglePropertyValueMerger) mergerManager.getPropertyValueMerger(mergerClass); //todo get the merger from the context
        return merger.mergeObjects(sourceValue, mergerExtraParam);
    }
    
    
    private Object applyRestoreToSingleField(Object originalValue, FieldMetadata fieldMetadata, int sourceValueIndex) {
        Class mergerClass = fieldMetadata.getSourceMergers()[sourceValueIndex];
        String[] mergerExtraParam = fieldMetadata.getSourceMergersParams()[sourceValueIndex];
        
        SinglePropertyValueMerger merger = (SinglePropertyValueMerger) mergerManager.getPropertyValueMerger(mergerClass); 
        
        if (merger.isRestoreSupported(mergerExtraParam)) {
            return merger.restoreObject(originalValue, mergerExtraParam);
        }
        
        return originalValue;
    }
    
    
    private Object readSourceValue(HashMap<String,Object> sourceBeans, String sourceProperty, FieldMetadata fieldMetadata, int sourceIndex) {

        String sourceBean = fieldMetadata.getSourceBeans()[sourceIndex];

        Object bo = sourceBeans.get(sourceBean);
        
        if (bo == null) {
            throw new IllegalStateException("could not find source bean with name: " + sourceBean);
        }
        
        //if it is the root object, then just return it.
        if (StringUtils.equals(sourceProperty, Binding.ROOT_OBJECT)) {
            return bo;
        }
        
        Object sourceValue = modifier.readPropertyValue(sourceProperty, bo);
        
        return sourceValue;
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

    public PropertyValueMergerInstanceManager getMergerManager() {
        return mergerManager;
    }

    public void setMergerManager(PropertyValueMergerInstanceManager mergerManager) {
        this.mergerManager = mergerManager;
    }
}
