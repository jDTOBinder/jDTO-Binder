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

import org.jdto.BeanModifier;
import org.jdto.DTOBinder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of the DTO binding lifecycle. Subclasses can extend this
 * in the way they find convenient by adding integration with frameworks or any 
 * additional feature.
 * @author Juan Alberto Lopez Cavallotti
 */
public class DTOBinderBean implements DTOBinder {

    private static final long serialVersionUID = 1L;
    private HashMap<Class, BeanMetadata> metadata;
    static final Logger logger = LoggerFactory.getLogger(DTOBinderBean.class);
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
     * Build a binder instance which will read its configuration from an
     * XML File received as the constructo parameter
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
        return implementationDelegate.bindFromBusinessObject(metadata, dtoClass, businessObjects);
    }

    @Override
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists) {
        //this will apply repeatedly the conversion results to a list.
        Object[] paramsBuffer = new Object[businessObjectsLists.length];

        List<T> ret = new ArrayList<T>();

        //the reference size will be the first list size
        int refSize = businessObjectsLists[0].size();

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
    }

    @Override
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto) {
        return implementationDelegate.extractFromDto(metadata, businessObjectClass, dto);
    }

    //GETTER + SETTER IMPLEMENTATION
    public HashMap<Class, BeanMetadata> getMetadata() {
        return metadata;
    }

    public final void setMetadata(HashMap<Class, BeanMetadata> metadata) {
        this.metadata = metadata;
    }

    public BeanModifier getBeanModifier() {
        return implementationDelegate.getModifier();
    }

    public void setBeanModifier(BeanModifier modifier) {
        this.implementationDelegate.setModifier(modifier);
    }
}
