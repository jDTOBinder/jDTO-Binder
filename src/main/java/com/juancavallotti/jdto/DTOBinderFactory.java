package com.juancavallotti.jdto;

import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Use this class to obtain instances of the DTO Binder bean.
 * @author juancavallotti
 */
public class DTOBinderFactory {

    /**
     * Create a new instance of a DTO binder with default settings. <br />
     * The resulting bean will read its configurations out of annotations on the
     * target DTOs
     * @return 
     */
    public static DTOBinder buildBinder() {
        DTOBinderBean bean = new DTOBinderBean();

        bean.setBeanModifier(new CoreBeanModifier());
        bean.setMetadata(new HashMap<Class, BeanMetadata>());

        return bean;
    }
    
    /**
     * Create a new instance of a DTO binder which will read the binding configuration
     * out of the given XML file passed as a parameter.
     * @param xmlFile
     * @return 
     */
    public static DTOBinder buildBinder(InputStream xmlFile) {
        DTOBinderBean bean = new DTOBinderBean(xmlFile);

        bean.setBeanModifier(new CoreBeanModifier());
        bean.setMetadata(new HashMap<Class, BeanMetadata>());

        return bean;

    }
}
