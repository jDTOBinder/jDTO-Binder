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

import java.util.Collection;
import org.jdto.BeanModifier;
import org.jdto.DTOBinder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.jdto.PropertyValueMerger;
import org.jdto.PropertyValueMergerInstanceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of the DTO binding lifecycle. Subclasses can extend this
 * in the way they find convenient by adding integration with frameworks or any
 * additional feature.
 *
 * @author Juan Alberto Lopez Cavallotti
 */
public class DTOBinderBean implements DTOBinder {

    private static final long serialVersionUID = 1L;
    private HashMap<Class, BeanMetadata> metadata;
    static final Logger logger = LoggerFactory.getLogger(DTOBinderBean.class);
    ThreadLocal<HashMap> bindingContext = new ThreadLocal<HashMap>();
    /**
     * This delegate will hold the real implementation of the binding lifecycle
     * for simple instances, hiding the complexity of other types of bindings
     * provided only for user convencience.
     */
    private final SimpleBinderDelegate implementationDelegate;

    /**
     * Please do not use this constructor directly unless you REALLY know what
     * you're doing.
     */
    public DTOBinderBean() {
        implementationDelegate = new SimpleBinderDelegate(this);

        //try to read xml configuration at default location.
        logger.debug("Trying to use default xml config file...");
        XMLBeanInspector xmlInspector = XMLBeanInspector.getInstance();

        if (xmlInspector != null) {
            logger.info("Using discovered configuration file: " + XMLBeanInspector.DEFAULT_PACKAGE_RESOURCE);
            implementationDelegate.setInspector(xmlInspector);
        } else {
            implementationDelegate.setInspector(new AnnotationBeanInspector());
        }
    }

    /**
     * Build a binder instance which will read its configuration from an XML
     * File received as the constructo parameter
     *
     * @param xmlFile an input stream for the XML config file.
     */
    public DTOBinderBean(InputStream xmlFile, boolean eagerLoad) {
        implementationDelegate = new SimpleBinderDelegate(this);
        XMLBeanInspector xmlInspector = new XMLBeanInspector(xmlFile);
        implementationDelegate.setInspector(xmlInspector);

        if (eagerLoad) {
            setMetadata(xmlInspector.buildMetadata());
        }

    }

    @Override
    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects) {

        boolean shouldReleaseThreadLocal = initBindingContextIfNecessary();
        try {
            if (businessObjects[0] == null) {
                return dtoClass.newInstance();            	
            }
            return implementationDelegate.bindFromBusinessObject(metadata, dtoClass, businessObjects);
        }catch(Exception e){
        	return null;
        } finally {
            releaseBindingContext(shouldReleaseThreadLocal);
        }
    }

    @Override
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists) {

        boolean shouldReleaseThreadLocal = initBindingContextIfNecessary();
        try {
            
            //this will apply repeatedly the conversion results to a list.
            Object[] paramsBuffer = new Object[businessObjectsLists.length];

            int refSize = getSourceListsSize(businessObjectsLists);
            
            //all of the elements are null
            if (refSize == -1) {
                return null;
            }
            
            
            List<T> ret = new ArrayList<T>();
            //repeatedly run the simple binding.
            for (int i = 0; i < refSize; i++) {
                for (int j = 0; j < businessObjectsLists.length; j++) {
                    List param = businessObjectsLists[j];
                    paramsBuffer[j] = param.get(i);
                }

                T result = bindFromBusinessObject(dtoClass, paramsBuffer);
                ret.add(result);
            }

            return ret;
        } finally {
            releaseBindingContext(shouldReleaseThreadLocal);
        }
    }

    @Override
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto) {
        return implementationDelegate.extractFromDto(metadata, businessObjectClass, dto);
    }
    
    @Override
    public <T> T extractFromDto2BussinessObject(Class<T> businessObjectClass, Object dto, Object businessObject) {    	
    	if (businessObject == null){
    		return implementationDelegate.extractFromDto(metadata, businessObjectClass, dto);
    	}else{
    		return implementationDelegate.extractFromDto2BussinesObject(metadata, businessObjectClass, dto, businessObject);
    	}
    }

    @Override
    public <T, R extends Collection> R bindFromBusinessObjectCollection(Class<T> dtoClass, R businessObjectsCollection) {
        boolean shouldReleaseThreadLocal = initBindingContextIfNecessary();
        try {

            if (businessObjectsCollection == null) {
                return null;
            }

            R ret = BeanClassUtils.createCollectionInstance(businessObjectsCollection.getClass());

            for (Object object : businessObjectsCollection) {
                T result = bindFromBusinessObject(dtoClass, object);
                ret.add(result);
            }

            return (R) ret;
        } finally {
            releaseBindingContext(shouldReleaseThreadLocal);
        }

    }

    @Override
    public <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass) {
        return implementationDelegate.getMergerManager().getPropertyValueMerger(mergerClass);
    }
    
    /**
     * Initializes the thread local and keeps track if it needs to be released.
     * @return true if this method created the context, false if not.
     */
    private boolean initBindingContextIfNecessary() {
        HashMap context = bindingContext.get();
        if (context == null) {
            bindingContext.set(new HashMap());
            return true;
        }

        return false;
    }
    
    /**
     * Releases the binding context if necessary
     * @param shouldRelease whether or not is necessary to release the binding context.
     */
    private void releaseBindingContext(boolean shouldRelease) {
        if (!shouldRelease) {
            return;
        }
        bindingContext.remove();
    }

    //GETTER + SETTER IMPLEMENTATION
    public HashMap<Class, BeanMetadata> getMetadata() {
        return metadata;
    }

    public final void setMetadata(HashMap<Class, BeanMetadata> metadata) {
        this.metadata = metadata;
    }

    public BeanModifier getBeanModifier() {
        return this.implementationDelegate.getModifier();
    }

    public void setBeanModifier(BeanModifier modifier) {
        this.implementationDelegate.setModifier(modifier);
    }

    public PropertyValueMergerInstanceManager getMergerManager() {
        return this.implementationDelegate.getMergerManager();
    }

    public void setMergerManager(PropertyValueMergerInstanceManager manager) {
        this.implementationDelegate.setMergerManager(manager);
    }
    
    //get the size of the first not-null list
    private int getSourceListsSize(List[] businessObjectsLists) {
        
        for (List list : businessObjectsLists) {
            if (list != null) {
                return list.size();
            }
        }
        
        return -1;
    }
}
