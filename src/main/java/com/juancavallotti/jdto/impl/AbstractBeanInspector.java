package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.MultiPropertyValueMerger;
import com.juancavallotti.jdto.SinglePropertyValueMerger;
import com.juancavallotti.jdto.mergers.FirstObjectPropertyValueMerger;
import com.juancavallotti.jdto.mergers.IdentityPropertyValueMerger;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for bean analysis, by implementing the template method pattern
 * this class is responsible for the default bean analysis cycle, this means
 * reading all of the fields on a given bean class and delegating to the concrete
 * implementation what to do for each field but providing the funcionality to
 * get default values as needed.
 * @author juancavallotti
 */
abstract class AbstractBeanInspector {

    static final Logger logger = LoggerFactory.getLogger(AbstractBeanInspector.class);

     <T> BeanMetadata inspectBean(Class<T> beanClass) {
        logger.debug("Strarting analysis of " + beanClass.toString());
        try {

            //the returned metadata.
            BeanMetadata ret = new BeanMetadata();

            HashMap<String, Method> beanGetters = BeanPropertyUtils.discoverPropertiesByGetters(beanClass);

            //the standard cycle for analyzing a bean.
            for (String propertyName : beanGetters.keySet()) {

                FieldMetadata metadata = buildFieldMetadata(propertyName, beanGetters.get(propertyName), beanClass);

                //if the field is transient, then do not add it.
                if (metadata.isFieldTransient()) {
                    //nothing to bind, this could happen if the field is tansient
                    //or not found.
                    continue;
                }

                logger.info("\tBound " + propertyName + " to " + metadata.getSourceFields().toString());


                ret.putFieldMetadata(propertyName, metadata);
            }


            String[] sourceBeanNames = readSourceBeanNames(beanClass);

            ret.setDefaultBeanNames(sourceBeanNames);

            return ret;

        } catch (Exception ex) {
            logger.error("Error while inspecting bean" + beanClass.toString(), ex);
            throw new RuntimeException("Could not inspect bean", ex);
        }
    }

    /**
     * Build the field metadata for a given field.
     * @param propertyName
     * @param readAccessor
     * @param beanClass 
     * @return 
     */
    abstract FieldMetadata buildFieldMetadata(String propertyName, Method readAccessor, Class beanClass);

    /**
     * Read the source names out of a class or whatever :)
     * @param beanClass
     * @return 
     */
    abstract String[] readSourceBeanNames(Class beanClass);
    
    /**
     * Build the default field metadata for a single field. This can be used either
     * to base your configuration on the standard and override as needed or
     * to just return it when no configuration is found.
     * @param propertyName
     * @return 
     */
    FieldMetadata buildDefaultFieldMetadata(String propertyName) {
        FieldMetadata ret = new FieldMetadata();
        
        //set the cascade
        ret.setCascadePresent(defaultCascadePresent());
        ret.setCascadeTargetClass(defaultCascadeTargetClass());
        ret.setCascadeType(defaultCascadeType());
        ret.setFieldTransient(defaultFieldTransient());
        ret.setMergerParameter(defaultMergerParameter());
        ret.setPropertyValueMerger(defaultMultiPropertyMerger());
        ret.setSourceBeanNames(defaultSourceBeanNames());
        ret.setSourceFields(Arrays.asList(propertyName));
        ret.setSourceMergers(defaultSinglePropertyMerger(propertyName));
        ret.setSourceMergersParams(defaultSinglePropertyMergerParams(propertyName));
        
        //and that is the default field metadata.
        //later on it can be used to customise it on other ways.
        
        return ret;
    }

    /**
     * Encapuslate the default value for cascade present.
     * @return 
     */
    boolean defaultCascadePresent() {
        return false;
    }
    
    /**
     * as the default cascade is false, then it is safe to set null
     * as the default cascade target class.
     * @return 
     */
    Class defaultCascadeTargetClass() {
        return null;
    }
    
    /**
     * The default cascade type is safe to be null also.
     * @return 
     */
    CascadeType defaultCascadeType() {
        return null;
    }
    
    /**
     * the default is all fields are not transient.
     * @return 
     */
    boolean defaultFieldTransient() {
        return false;
    }

    /**
     * An empty string is the default merger parameter.
     * @return 
     */
    String defaultMergerParameter() {
        return "";
    }
    
    /**
     * The default multi property merger is the first value.
     * @return 
     */
    MultiPropertyValueMerger defaultMultiPropertyMerger() {
        return InstancePool.getOrCreate(FirstObjectPropertyValueMerger.class);
    }

    
    //to save energy
    private static final String[] defaultSrouceBeanNames = {""};
    
    /**
     * The default source bean names. DO NOT CHANGE!!
     * @return 
     */
    String[] defaultSourceBeanNames() {
        return defaultSrouceBeanNames;
    }
    
    /**
     * The default single property merger map.
     * @param propertyName
     * @return 
     */
    HashMap<String, SinglePropertyValueMerger> defaultSinglePropertyMerger(String propertyName) {
        HashMap<String, SinglePropertyValueMerger> ret = new HashMap<String, SinglePropertyValueMerger>();
        ret.put(propertyName, defaultSinglePropertyMerger());
        return ret;
    }
    
    /**
     * The default single property merger params.
     * @param propertyName
     * @return 
     */
    HashMap<String, String> defaultSinglePropertyMergerParams(String propertyName) {
        HashMap<String, String> ret = new HashMap<String, String>();
        ret.put(propertyName, "");
        return ret;
    }
    
    /**
     * The default single property merger instance.
     * @return 
     */
    SinglePropertyValueMerger defaultSinglePropertyMerger() {
        return InstancePool.getOrCreate(IdentityPropertyValueMerger.class);
    }
   
}
