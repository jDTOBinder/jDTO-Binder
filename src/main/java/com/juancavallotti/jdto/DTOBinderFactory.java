package com.juancavallotti.jdto;

import com.juancavallotti.jdto.impl.BeanMetadata;
import com.juancavallotti.jdto.impl.CoreBeanModifier;
import com.juancavallotti.jdto.impl.DTOBinderBean;
import java.util.HashMap;

/**
 *
 * @author juancavallotti
 */
public class DTOBinderFactory {
    
    /**
     * Create a new instance of a DTO binder with default settings.
     * @return 
     */
    public static DTOBinder buildBinder() {
        DTOBinderBean bean = new DTOBinderBean();
        
        //for now depend on the spring framework implementation until I build my own.
        bean.setBeanModifier(new CoreBeanModifier());
        bean.setMetadata(new HashMap<Class, BeanMetadata>());
        
        return bean;
    }
    
}
