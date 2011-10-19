package com.juancavallotti.jdto;

import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import com.juancavallotti.jdto.impl.InstancePool;
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

        bean.setBeanModifier(getBeanModifier());
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
        DTOBinderBean bean = new DTOBinderBean(xmlFile, true);
        bean.setBeanModifier(getBeanModifier());
        return bean;
    }

    /**
     * Get a singleton instance of a DTO binder. This will cache the object
     * in the instance pool and will retrieve it on further usage. <br />
     * 
     * It is recommended to use this method if you want the instance being
     * handled or remembered for you. <br />
     * 
     * The returned DTO binder is annotation-style configured, which is also
     * recommended.
     * @return 
     */
    public static DTOBinder getBinder() {
        DTOBinderBean bean = InstancePool.getOrCreate(DTOBinderBean.class);
        bean.setBeanModifier(getBeanModifier());

        if (bean.getMetadata() == null || bean.getMetadata().isEmpty()) {
            bean.setMetadata(new HashMap<Class, BeanMetadata>());
        }
        return bean;
    }

    private static BeanModifier getBeanModifier() {
        return InstancePool.getOrCreate(CoreBeanModifier.class);
    }
}
