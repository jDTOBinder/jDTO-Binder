package com.juancavallotti.jdto.cdi;

import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import java.util.HashMap;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for injecting via CDI. <br />
 * 
 * In order to use this bean you MUST add the following dependency:
 * <br />
 * Grroup ID: javax.inject <br />
 * Artifact ID: javax.inject <br />
 * Version: 1 (or greater).
 * 
 * <br />
 * Basic usage: <br />
 * 
 * <pre>
 * 
 * //field
 * &#64;Inject
 * &#64;CdiDtoBinder
 * private DTOBinder binder; 
 * 
 * </pre>
 * 
 * @author juancavallotti
 */
@Singleton
@CdiDtoBinder
public final class CdiDtoBinderBean extends DTOBinderBean {
        
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CdiDtoBinderBean.class);
    
    /**
     * This uses the core bean modifier and empty metadata.
     */
    public CdiDtoBinderBean() {
        logger.debug("Begin building SpringDTOBinder bean.");
        setBeanModifier(new CoreBeanModifier());
        logger.debug("BeanModifier is BeanWrapperBeanModofier");
        setMetadata(new HashMap<Class, BeanMetadata>());
        logger.debug("Created blank metadata map.");
    }
}
