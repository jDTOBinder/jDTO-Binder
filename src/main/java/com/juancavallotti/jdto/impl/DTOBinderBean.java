package com.juancavallotti.jdto.impl;

import com.juancavallotti.jdto.BeanModifier;
import com.juancavallotti.jdto.DTOBinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Basic implementation of the DTO binding lifecycle. Subclasses can extend this
 * in the way they find convenient by adding integration with frameworks or any 
 * additional feature.
 * @author juancavallotti
 */
public class DTOBinderBean implements DTOBinder {
    private static final long serialVersionUID = 1L;
    
    private HashMap<Class, BeanMetadata> metadata;
    
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
        implementationDelegate.setInspector(new BeanInspector());
    }

    public <T> T bindFromBusinessObject(Class<T> dtoClass, Object... businessObjects) {
        return implementationDelegate.bindFromBusinessObject(metadata, dtoClass, businessObjects);
    }

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

    public <T> T extractFromDto(Class<T> businessObjectClass, Object dto) {
        return implementationDelegate.extractFromDto(metadata, businessObjectClass, dto);
    }
    
    //GETTER + SETTER IMPLEMENTATION

    public HashMap<Class, BeanMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<Class, BeanMetadata> metadata) {
        this.metadata = metadata;
    }
    
    public BeanModifier getBeanModifier() {
        return implementationDelegate.getModifier();
    }

    public void setBeanModifier(BeanModifier modifier) {
        this.implementationDelegate.setModifier(modifier);
    }
}
