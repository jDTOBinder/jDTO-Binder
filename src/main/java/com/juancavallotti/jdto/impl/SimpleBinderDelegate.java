package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.BeanModifier;
import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for binding simple instances of beans.
 * @author juancavallotti
 */
class SimpleBinderDelegate implements Serializable {

    private static final long serialVersionUID = 1L;
    private final DTOBinderBean binderBean;
    private BeanInspector inspector;
    private BeanModifier modifier;
    private static final Logger logger = LoggerFactory.getLogger(SimpleBinderDelegate.class);

    SimpleBinderDelegate(DTOBinderBean binderBean) {
        inspector = new BeanInspector();
        this.binderBean = binderBean;
    }

     <T> T bindFromBusinessObject(HashMap<Class, BeanMetadata> metadataMap, Class<T> dtoClass, Object... businessObjects) {
        //first of all, we try to find the metadata for the DTO to build, if not
        //then we build it.
        BeanMetadata metadata = findBeanMetadata(metadataMap, dtoClass);

        //for now we only accept one business object.
        Object bo = businessObjects[0];

        T ret = BeanInstanceUtils.createInstance(dtoClass);

        HashMap<String, FieldMetadata> propertyMappings = metadata.getFieldMetadata();
        //iterate through the properties and read the values from the business objects.
        for (String targetProperty : propertyMappings.keySet()) {
            
            //get the configuration for the DTO objects.
            FieldMetadata fieldMetadata = propertyMappings.get(targetProperty);
            
            //create a buffer for the source values.
            List<Object> sourceValues = new ArrayList<Object>();
            
            //get a list of the properties to read for this DTO
            List<String> sourceProperties = fieldMetadata.getSourceFields();
            
            //read all the values.
            for (String sourceProperty : sourceProperties) {
                Object sourceValue = modifier.readPropertyValue(sourceProperty, bo);
                Object finalValue = applyMergeToSingleField(sourceValue, sourceProperty, fieldMetadata);
                sourceValues.add(finalValue);
            }
            
            
            //merge the values into one
            MultiPropertyValueMerger merger = fieldMetadata.getPropertyValueMerger();
            
            Object targetValue = null;

            if (fieldMetadata.isCascadePresent()) {
                targetValue = applyCascadeLogic(sourceValues, fieldMetadata);
            } else {
                targetValue = merger.mergeObjects(sourceValues, fieldMetadata.getMergerParameter());
            }

            modifier.writePropertyValue(targetProperty, targetValue, ret);
        }

        return ret;
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
            return new ArrayList((Collection)value);
        }
        
        if (value.getClass().isArray()) {
            return Arrays.asList((Object[])value);
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
        
        T ret = BeanInstanceUtils.createInstance(entityClass);
        
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
    
    private Object applyMergeToSingleField(Object sourceValue, String sourceProperty, FieldMetadata fieldMetadata) {
        SinglePropertyValueMerger merger = fieldMetadata.getSourceMergers().get(sourceProperty);
        String mergerExtraParam = fieldMetadata.getSourceMergersParams().get(sourceProperty);
        
        return merger.mergeObjects(sourceValue, mergerExtraParam);
    }
    
    //GETTERS AND SETTERS
    public BeanInspector getInspector() {
        return inspector;
    }

    public void setInspector(BeanInspector inspector) {
        this.inspector = inspector;
    }

    public BeanModifier getModifier() {
        return modifier;
    }

    public void setModifier(BeanModifier modifier) {
        this.modifier = modifier;
    }

}
