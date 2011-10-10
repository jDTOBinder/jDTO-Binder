package com.juancavallotti.jdto.spring;

import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DTO Binder to use as a Spring bean. In the future this bean will have the ability
 * to scan packages for DTOs in order to build and cache the metadata when this
 * bean is instanciated for the first time.
 * 
 * @author juancavallotti
 */
public class SpringDTOBinder extends DTOBinderBean {

    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(SpringDTOBinder.class);
    
    public SpringDTOBinder() {
        logger.debug("Begin building SpringDTOBinder bean.");
        setBeanModifier(new BeanWrapperBeanModifier());
        logger.debug("BeanModifier is BeanWrapperBeanModofier");
        setMetadata(new HashMap<Class, BeanMetadata>());
        logger.debug("Created blank metadata map.");
    }
}
