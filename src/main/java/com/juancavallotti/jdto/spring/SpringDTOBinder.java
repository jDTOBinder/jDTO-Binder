package com.juancavallotti.jdto.spring;

import com.juancavallotti.jdto.DTOBinder;
import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * DTO Binder to use as a Spring bean. In the future this bean will have the ability
 * to scan packages for DTOs in order to build and cache the metadata when this
 * bean is instanciated for the first time.
 * 
 * @author juancavallotti
 */
public class SpringDTOBinder implements InitializingBean, DTOBinder {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SpringDTOBinder.class);
    private DTOBinderBean delegate;
    private Resource xmlConfig;

    public SpringDTOBinder() {
        //do nothing on the constructor.
    }

    /**
     * Default init method for spring framework.
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

    }

    @Override
    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects) {
        return delegate.bindFromBusinessObject(dtoClass, businessObjects);
    }

    @Override
    public <T> List<T> bindFromBusinessObjectList(Class<T> dtoClass, List... businessObjectsLists) {
        return delegate.bindFromBusinessObjectList(dtoClass, businessObjectsLists);
    }

    @Override
    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto) {
        return delegate.extractFromDto(businessObjectClass, dto);
    }

    public Resource getXmlConfig() {
        return xmlConfig;
    }

    public void setXmlConfig(Resource xmlConfig) {
        this.xmlConfig = xmlConfig;
    }
}
