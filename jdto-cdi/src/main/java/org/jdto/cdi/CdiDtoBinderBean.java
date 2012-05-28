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

package org.jdto.cdi;

import java.util.HashMap;
import javax.inject.Singleton;
import org.jdto.impl.BeanMetadata;
import org.jdto.impl.CoreBeanModifier;
import org.jdto.impl.DTOBinderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for injecting via CDI. <br />
 * 
 * In order to use this bean you MUST add the following dependency 
 * (typically provided on a CDI-enabled environment):
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
 * private DTOBinder binder; 
 * 
 * </pre>
 * 
 * @author Juan Alberto Lopez Cavallotti
 */
@Singleton
public final class CdiDtoBinderBean extends DTOBinderBean {
        
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(CdiDtoBinderBean.class);
    
    /**
     * This uses the core bean modifier and empty metadata.
     */
    public CdiDtoBinderBean() {
        logger.debug("Begin building CdiDtoBinderBean.");
        setBeanModifier(new CoreBeanModifier());
        logger.debug("BeanModifier is BeanWrapperBeanModofier");
        setMetadata(new HashMap<Class, BeanMetadata>());
        logger.debug("Created blank metadata map.");
    }
}
