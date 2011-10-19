package com.juancavallotti.jdto;

/**
 * Makes this object aware of the current bean modifier used by the DTO binding
 * instance.
 * @author juan
 */
public interface BeanModifierAware {
    
    /**
     * Expose the BeanModifer to the implementing class.
     * @param modifier 
     */
    void setBeanModifier(BeanModifier modifier);
}
