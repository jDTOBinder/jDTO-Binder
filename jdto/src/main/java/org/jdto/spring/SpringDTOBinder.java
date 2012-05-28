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

package org.jdto.spring;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.jdto.DTOBinder;
import org.jdto.PropertyValueMerger;
import org.jdto.impl.BaseMergerInstanceManager;
import org.jdto.impl.BeanMetadata;
import org.jdto.impl.DTOBinderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * DTO Binder to use as a Spring bean. In the future this bean will have the ability
 * to scan packages for DTOs in order to build and cache the metadata when this
 * bean is instanciated for the first time. <br />
 * 
 * The following snippet shows the typical configuration of this spring bean.
 * <!-- HTML generated using hilite.me --><div style="background: #ffffff; overflow:auto;width:auto;color:black;background:white;border:solid gray;border-width:.1em .1em .1em .8em;padding:.2em .6em;"><pre style="margin: 0; line-height: 125%"><span style="color: #808080">&lt;!-- THE DTO BINDER BEAN --&gt;</span> 
 *<span style="color: #007000">&lt;bean</span> <span style="color: #0000C0">id=</span><span style="background-color: #fff0f0">&quot;dtobinder&quot;</span> 
 *   <span style="color: #0000C0">class=</span><span style="background-color: #fff0f0">&quot;org.jdto.spring.SpringDTOBinder&quot;</span> <span style="color: #007000">/&gt;</span>
 *</pre></div>
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
public class SpringDTOBinder implements InitializingBean, DTOBinder {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SpringDTOBinder.class);
    private DTOBinderBean delegate;
    private Resource xmlConfig;

    /**
     * Build a new instance of the SpringDTOBinder bean.
     */
    public SpringDTOBinder() {
        //do nothing on the constructor.
    }

    /**
     * Default init method defined by spring framework.
     */
    @Override
    public void afterPropertiesSet() {

        logger.debug("Begin building SpringDTOBinder bean.");
        
        
        if (xmlConfig != null) {
            try {
                logger.debug("Configured with XML settings");
                delegate = new DTOBinderBean(xmlConfig.getInputStream(), true);
            } catch (Exception ex) {
                logger.error("Failed to load with xml configuration", ex);
            }
        } else {
            delegate = new DTOBinderBean();
            logger.debug("Created blank metadata map.");
            delegate.setMetadata(new HashMap<Class, BeanMetadata>());
        }

        logger.debug("BeanModifier is BeanWrapperBeanModofier");
        delegate.setBeanModifier(new BeanWrapperBeanModifier());
        BaseMergerInstanceManager mergerManager = new BaseMergerInstanceManager();
        mergerManager.setModifier(delegate.getBeanModifier());
        delegate.setMergerManager(mergerManager);
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects) {
        return delegate.bindFromBusinessObject(dtoClass, businessObjects);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists) {
        return delegate.bindFromBusinessObjectList(dtoClass, businessObjectsLists);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto) {
        return delegate.extractFromDto(businessObjectClass, dto);
    }

    /**
     * Get the resource where to find the XML configuration file.
     * @return the resource or null.
     */
    public Resource getXmlConfig() {
        return xmlConfig;
    }
    
    /**
     * Set the resource where to find the XML Configuration file.
     * @param xmlConfig the resource where the XML configuration file can be loaded.
     */
    public void setXmlConfig(Resource xmlConfig) {
        this.xmlConfig = xmlConfig;
    }
    
    /**
     * {@inheritDoc }
     * @since 1.1
     */
    @Override
    public <T,R extends Collection> R bindFromBusinessObjectCollection(Class<T> dtoClass, R businessObjects) {
        return delegate.bindFromBusinessObjectCollection(dtoClass, businessObjects);
    }
    
    /**
     * {@inheritDoc}
     * @since 1.2
     */
    @Override
    public <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass) {
        return delegate.getPropertyValueMerger(mergerClass);
    }
}
